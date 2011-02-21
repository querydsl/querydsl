/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.Variables;
import com.mysema.query.codegen.Delegate;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Method;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.TypeFactory;
import com.mysema.query.codegen.TypeMappings;

/**
 * Processor handles the actual work in the Querydsl APT module
 *
 * @author tiwe
 *
 */
public class Processor {

    /**
     * Cache for annotated elements
     */
    static final Map<Class<? extends Annotation>, Set<Element>> elementCache = new HashMap<Class<? extends Annotation>,Set<Element>>();

    /**
     * Mapping of entity types to TypeElements which contribute to the generated class
     */
    private final Map<String,Set<TypeElement>> typeElements = new HashMap<String,Set<TypeElement>>();

    private final Map<String, EntityType> actualSupertypes  = new HashMap<String, EntityType>();

    private final Map<String, EntityType> allSupertypes = new HashMap<String, EntityType>();

    private final Configuration configuration;

    private final Map<String, EntityType> dtos = new HashMap<String, EntityType>();

    private final ElementHandler elementHandler;

    private final Map<String,EntityType> embeddables = new HashMap<String,EntityType>();

    private final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();

    private final ProcessingEnvironment env;

    private final Map<String,EntityType> extensionTypes = new HashMap<String,EntityType>();

    private final RoundEnvironment roundEnv;

    private final ExtendedTypeFactory typeFactory;

    /**
     * Create a new Processor instance
     *
     * @param env
     * @param roundEnv
     * @param configuration
     */
    public Processor(ProcessingEnvironment env, RoundEnvironment roundEnv, Configuration configuration) {
        this.env = Assert.notNull(env,"env");
        this.roundEnv = Assert.notNull(roundEnv,"roundEnv");
        this.configuration = Assert.notNull(configuration,"configuration");

        List<Class<? extends Annotation>> anns = new ArrayList<Class<? extends Annotation>>();
        anns.add(configuration.getEntityAnnotation());
        if (configuration.getSuperTypeAnnotation() != null){
            anns.add(configuration.getSuperTypeAnnotation());
        }
        if (configuration.getEmbeddableAnnotation() != null){
            anns.add(configuration.getEmbeddableAnnotation());
        }

        TypeFactory factory = new TypeFactory(anns);
        this.typeFactory = new ExtendedTypeFactory(env, configuration, factory, anns);
        this.elementHandler = new ElementHandler(configuration, typeFactory);
    }

    public void process() {
        processAnnotations();

        // remove entity types from extensionTypes
        for (String key : entityTypes.keySet()){
            extensionTypes.remove(key);
        }

        serializeTypes();

        serializeVariableClasses();
    }

    private void processAnnotations() {
        processDelegateMethods();

        processCustomTypes();

        processExtensions();

        if (configuration.getSuperTypeAnnotation() != null) {
            processSupertypes();
        }

        if (configuration.getEmbeddedAnnotation() != null){
            processEmbedded();
        }

        if (configuration.getEmbeddableAnnotation() != null){
            processEmbeddables();
        }

        if (configuration.getEntitiesAnnotation() != null){
            processEntitiesFromPackage();
        }

        processEntities();

        processDTOs();
    }

    private void serializeTypes() {
        if (!actualSupertypes.isEmpty()){
            env.getMessager().printMessage(Kind.NOTE, "Serializing Supertypes");
            serialize(configuration.getSupertypeSerializer(), actualSupertypes.values());
        }

        if (!entityTypes.isEmpty()){
            env.getMessager().printMessage(Kind.NOTE, "Serializing Entity types");
            serialize(configuration.getEntitySerializer(), entityTypes.values());
        }

        if (!extensionTypes.isEmpty()){
            env.getMessager().printMessage(Kind.NOTE, "Serializing Extension types");
            serialize(configuration.getEmbeddableSerializer(), extensionTypes.values());
        }

        if (!embeddables.isEmpty()){
            env.getMessager().printMessage(Kind.NOTE, "Serializing Embeddable types");
            serialize(configuration.getEmbeddableSerializer(), embeddables.values());
        }

        if (!dtos.isEmpty()){
            env.getMessager().printMessage(Kind.NOTE, "Serializing DTO types");
            serialize(configuration.getDTOSerializer(), dtos.values());
        }
    }

    private void serializeVariableClasses() {
        for (Element element : roundEnv.getElementsAnnotatedWith(Variables.class)){
            if (element instanceof PackageElement){
                Variables vars = element.getAnnotation(Variables.class);
                PackageElement packageElement = (PackageElement)element;
                List<EntityType> models = new ArrayList<EntityType>();
                for (EntityType model : entityTypes.values()){
                    if (model.getPackageName().equals(packageElement.getQualifiedName().toString())){
                        models.add(model);
                    }
                }
                serializeVariableList(packageElement.getQualifiedName().toString(), vars, models);
            }
        }
    }


    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes, Set<EntityType> handled) {
        if (handled.add(model)){
            for (Supertype supertype : model.getSuperTypes()){
                EntityType entityType = superTypes.get(supertype.getType().getFullName());
                if (entityType != null){
                    addSupertypeFields(entityType, superTypes, handled);
                    supertype.setEntityType(entityType);
                    model.include(supertype);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Element> getElementsAndCache(Class<? extends Annotation> annotationType){
        Set<Element> elements = (Set<Element>)roundEnv.getElementsAnnotatedWith(annotationType);
        if (!elements.isEmpty()){
            Set<Element> cached = elementCache.get(annotationType);
            if (cached == null){
                cached = new HashSet<Element>();
                elementCache.put(annotationType, cached);
            }
            cached.addAll(elements);
        }
        return elements;
    }

    private Set<Element> getElementsFromCache(Class<? extends Annotation> annotationType){
        Set<Element> cached = elementCache.get(annotationType);
        if (cached != null){
            Set<Element> cloned = new HashSet<Element>();
            for (Element element : cached){
                // clone type element
                if (element instanceof TypeElement){
                    cloned.add(env.getElementUtils().getTypeElement(((TypeElement) element).getQualifiedName().toString()));
                // cloned other elements via parent
                }else{
                    Element parent = element.getEnclosingElement();
                    if (parent instanceof TypeElement){
                        parent = env.getElementUtils().getTypeElement(((TypeElement) parent).getQualifiedName().toString());
                        for (Element child : parent.getEnclosedElements()){
                            // TODO : better equals check
                            if (child.getKind() == element.getKind() && child.getSimpleName().equals(element.getSimpleName())){
                                cloned.add(child);
                            }
                        }
                    }else{
                        env.getMessager().printMessage(Kind.WARNING, element + " from cache");
                        cloned.add(element);
                    }
                }
            }
            return cloned;
        }else{
            return Collections.emptySet();
        }
    }

    @Nullable
    private FileObject getSourceFile(String fullName) throws IOException {
        Elements elementUtils = env.getElementUtils();
        TypeElement sourceElement = elementUtils.getTypeElement(fullName);
        if (sourceElement == null) {
            return null;
        } else {
            if (sourceElement.getNestingKind().isNested()) {
                sourceElement = (TypeElement) sourceElement.getEnclosingElement();
            }
            PackageElement packageElement = elementUtils.getPackageOf(sourceElement);
            try {
                return env.getFiler().getResource(StandardLocation.SOURCE_PATH,
                        packageElement.getQualifiedName(),
                        sourceElement.getSimpleName() + ".java");
            } catch(Exception e) {
                return null;
            }
        }
    }

    private void handleExtensionType(TypeMirror type, TypeElement element, boolean cached) {
        EntityType entityModel = typeFactory.getEntityType(type, true);
        registerTypeElement(entityModel.getFullName(), element);
        // handle methods
        Set<Method> queryMethods = new HashSet<Method>();
        for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())){
            if (method.getAnnotation(QueryMethod.class) != null){
                elementHandler.handleQueryMethod(entityModel, method, queryMethods);
            }
        }
        for (Method method : queryMethods){
            entityModel.addMethod(method);
        }
        if (!cached){
            extensionTypes.put(entityModel.getFullName(), entityModel);
        }
    }

    private void mergeTypes(Map<String, EntityType> types, Deque<Type> superTypes) {
        // get external supertypes
        while (!superTypes.isEmpty()){
            Type superType = superTypes.pop();
            if (!types.containsKey(superType.getFullName())  && !allSupertypes.containsKey(superType.getFullName())){
                TypeElement typeElement = env.getElementUtils().getTypeElement(superType.getFullName());
                if (typeElement == null){
                    throw new IllegalStateException("Found no type for " + superType.getFullName());
                }
                EntityType entityType = elementHandler.handleNormalType(typeElement);
                if (entityType.getSuperType() != null){
                    superTypes.push(entityType.getSuperType().getType());
                }
                types.put(superType.getFullName(), entityType);
            }
        }

        allSupertypes.putAll(types);

        // add supertype fields
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType type : types.values()) {
            addSupertypeFields(type, allSupertypes, handled);
        }
    }

    private void process(Class<? extends Annotation> annotation, Map<String,EntityType> types){
        Deque<Type> superTypes = new ArrayDeque<Type>();

        // only creation
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnnotation() == null
                    || element.getAnnotation(configuration.getEmbeddableAnnotation()) == null){
                typeFactory.getEntityType(element.asType(), false);
                typeMirrors.add(element.asType());
            }
        }

        // super type handling
        for (TypeMirror typeMirror : typeMirrors){
            typeFactory.getEntityType(typeMirror, true);
        }

        // get annotated types
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnnotation() == null
                    || element.getAnnotation(configuration.getEmbeddableAnnotation()) == null){
                EntityType model = elementHandler.handleNormalType((TypeElement) element);
                registerTypeElement(model.getFullName(), (TypeElement)element);
                types.put(model.getFullName(), model);
                if (model.getSuperType() != null){
                    superTypes.push(model.getSuperType().getType());
                }
            }
        }

        mergeTypes(types, superTypes);
    }

    private void processCustomType(Element element, boolean cached){
        if (element.getAnnotation(QueryExtensions.class) != null){
            return;
        }else if (element.getAnnotation(configuration.getEntityAnnotation()) != null){
            return;
        }else if (configuration.getSuperTypeAnnotation() != null && element.getAnnotation(configuration.getSuperTypeAnnotation()) != null){
            return;
        }else if (configuration.getEmbeddableAnnotation() != null && element.getAnnotation(configuration.getEmbeddableAnnotation()) != null){
            return;
        }
        handleExtensionType(element.asType(), (TypeElement)element, cached);
    }

    private void processCustomTypes() {
        // 1 : get elements from cache
        for (Element queryMethod : getElementsFromCache(QueryMethod.class)){
            processCustomType(queryMethod.getEnclosingElement(), true);
        }

        // 2 : get elements from roundEnv
        for (Element queryMethod : getElementsAndCache(QueryMethod.class)){
            processCustomType(queryMethod.getEnclosingElement(), false);
        }
    }

    private void processDelegateMethod(Element delegateMethod, boolean cached) {
        ExecutableElement method = (ExecutableElement)delegateMethod;
        Element element = delegateMethod.getEnclosingElement();
        String name = method.getSimpleName().toString();
        Type delegateType = typeFactory.getType(element.asType(), true);
        Type returnType = typeFactory.getType(method.getReturnType(), true);
        List<Parameter> parameters = elementHandler.transformParams(method.getParameters());
        // remove first element
        parameters = parameters.subList(1, parameters.size());

        EntityType entityType = null;
        for (AnnotationMirror annotation : delegateMethod.getAnnotationMirrors()){
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(QueryDelegate.class.getSimpleName())){
                for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : annotation.getElementValues().entrySet()){
                    if (entry.getKey().getSimpleName().toString().equals("value")){
                        if (entry.getValue().getValue() instanceof TypeMirror){
                            TypeMirror type = (TypeMirror)entry.getValue().getValue();
                            entityType = typeFactory.getEntityType(type, true);
                        }
                    }
                }
            }
        }

        if (entityType != null){
            registerTypeElement(entityType.getFullName(), (TypeElement)element);
            entityType.addDelegate(new Delegate(entityType, delegateType, name, parameters, returnType));
            if (!cached && entityType.getOriginalCategory() != TypeCategory.SIMPLE){
                extensionTypes.put(entityType.getFullName(), entityType);
            }
        }
    }

    private void processDelegateMethods(){
        // 1 : get elements from cache
        for (Element delegateMethod : getElementsFromCache(QueryDelegate.class)){
            processDelegateMethod(delegateMethod, true);
        }

        // 2 : get elements from roundEnv
        for (Element delegateMethod : getElementsAndCache(QueryDelegate.class)){
            processDelegateMethod(delegateMethod, false);
        }
    }

    private void processDTOs() {
        Set<Element> visitedDTOTypes = new HashSet<Element>();
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (parent.getAnnotation(configuration.getEntityAnnotation()) == null
                    && parent.getAnnotation(configuration.getEmbeddableAnnotation()) == null
                    && !visitedDTOTypes.contains(parent)){
                EntityType model = elementHandler.handleProjectionType((TypeElement)parent);
                registerTypeElement(model.getFullName(), (TypeElement)parent);
                dtos.put(model.getFullName(), model);
                visitedDTOTypes.add(parent);

            }
        }
    }

    private void processEmbeddables() {
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();

        // only creation
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnnotation())) {
            typeFactory.getEntityType(element.asType(), false);
            typeMirrors.add(element.asType());
        }

        // supertype handling
        for (TypeMirror typeMirror : typeMirrors){
            typeFactory.getEntityType(typeMirror, true);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnnotation())) {
            EntityType model = elementHandler.handleNormalType((TypeElement) element);
            registerTypeElement(model.getFullName(), (TypeElement)element);
            embeddables.put(model.getFullName(), model);
        }
        allSupertypes.putAll(embeddables);

        // add super type fields
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, allSupertypes, handled);
        }
    }

    private void processEmbedded(){
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();
        
        // only creation
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddedAnnotation())) {
            TypeMirror type = element.asType();
            if (element.getKind() == ElementKind.METHOD){
                type = ((ExecutableElement)element).getReturnType();
            }            
            String typeName = type.toString();
            if (typeName.startsWith(Collection.class.getName())
             || typeName.startsWith(List.class.getName())
             || typeName.startsWith(Set.class.getName())){
                type = ((DeclaredType)type).getTypeArguments().get(0);
                
            }else if (typeName.startsWith(Map.class.getName())){
                type = ((DeclaredType)type).getTypeArguments().get(1);
            }   
            typeFactory.getEntityType(type, false);
            typeMirrors.add(type);
        }
        
        // supertype handling
        for (TypeMirror typeMirror : typeMirrors){
            typeFactory.getEntityType(typeMirror, true);
        }
        
        // deep
        for (TypeMirror type : typeMirrors) {
            // remove generic signature of type for TypeElement lookup
            String typeName = type.toString();
            if (typeName.contains("<")){
                typeName = typeName.substring(0, typeName.indexOf("<"));
            }
            TypeElement typeElement = env.getElementUtils().getTypeElement(typeName);
            if (typeElement.getAnnotation(configuration.getEntityAnnotation()) != null){
                // skip Entity types here
                continue;
            }
            
            EntityType model = elementHandler.handleNormalType(typeElement);
            registerTypeElement(model.getFullName(), typeElement);
            embeddables.put(model.getFullName(), model);
        }
        allSupertypes.putAll(embeddables);

        // add super type fields
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType embeddable : embeddables.values()) {
            addSupertypeFields(embeddable, allSupertypes, handled);
        }
    }

    private void processEntities() {
        process(configuration.getEntityAnnotation(), entityTypes);
    }

    @SuppressWarnings("unchecked")
    private void processEntitiesFromPackage(){
        Class<? extends Annotation> annotation = configuration.getEntitiesAnnotation();
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();

        // only creation
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)){
            for (AnnotationMirror mirror : element.getAnnotationMirrors()){
                if (mirror.getAnnotationType().asElement().getSimpleName().toString().equals(QueryEntities.class.getSimpleName())){
                    for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()){
                        if (entry.getKey().getSimpleName().toString().equals("value")){
                            List<AnnotationValue> values = (List<AnnotationValue>) entry.getValue().getValue();
                            for (AnnotationValue value : values){
                                TypeMirror type = (TypeMirror) value.getValue();
                                typeFactory.getEntityType(type, false);
                                typeMirrors.add(type);
                            }
                        }
                    }
                }
            }
        }

        Map<String,EntityType> types = entityTypes;
        Deque<Type> superTypes = new ArrayDeque<Type>();

        // get annotated types
        for (TypeMirror mirror : typeMirrors){
            typeFactory.getEntityType(mirror, true);
            TypeElement element = (TypeElement) env.getTypeUtils().asElement(mirror);
            EntityType model = elementHandler.handleNormalType(element);
            registerTypeElement(model.getFullName(), element);
            types.put(model.getFullName(), model);
            if (model.getSuperType() != null){
                superTypes.push(model.getSuperType().getType());
            }
        }

        mergeTypes(types, superTypes);
    }

    private void processExtension(Element element, boolean cached) {
        for (AnnotationMirror annotation : element.getAnnotationMirrors()){
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(QueryExtensions.class.getSimpleName())){
                for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : annotation.getElementValues().entrySet()){
                    if (entry.getKey().getSimpleName().toString().equals("value")){
                        if (entry.getValue().getValue() instanceof TypeMirror){
                            TypeMirror type = (TypeMirror)entry.getValue().getValue();
                            handleExtensionType(type, (TypeElement)element, cached);
                        }
                    }
                }
            }
        }
    }

    private void processExtensions() {
        // 1st : get elements from cache
        for (Element element : getElementsFromCache(QueryExtensions.class)){
            processExtension(element, true);
        }

        // 2nd : get elements from roundEnv
        for (Element element : getElementsAndCache(QueryExtensions.class)){
            processExtension(element, false);
        }
    }

    private void processSupertypes() {
        process(configuration.getSuperTypeAnnotation(), actualSupertypes);
    }

    private void registerTypeElement(String entityName, TypeElement element){
        Set<TypeElement> elements = typeElements.get(entityName);
        if (elements == null){
            elements = new HashSet<TypeElement>();
            typeElements.put(entityName, elements);
        }
        elements.add(element);
    }

    private void serialize(Serializer serializer, Collection<EntityType> models) {
        Messager msg = env.getMessager();
        for (EntityType model : models) {
            try {
                String packageName = model.getPackageName();
                Type type = configuration.getTypeMappings().getPathType(model, model, true);
                String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();

                Filer filer = env.getFiler();
                FileObject generatedFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, type.getSimpleName() + ".java");
                boolean generate = false;

                Set<TypeElement> elements = typeElements.get(model.getFullName());
                if (elements != null){
                    boolean foundSources = false;
                    for (TypeElement element : elements){
                        FileObject sourceFile = getSourceFile(element.getQualifiedName().toString());
                        // Source file is accessible and exists
                        if (sourceFile != null && sourceFile.getLastModified() > 0) {
                            foundSources = true;
                            // Generate if source has changed since Q-type was last time generated
                            generate |= generatedFile.getLastModified() <= sourceFile.getLastModified();
                        }
                    }
                    if (!foundSources){
                        if (configuration.isDefaultOverwrite()) {
                            generate = true;
                        } else {
                            generate = generatedFile.getLastModified() <= 0;
                        }
                    }

                } else {
                    // Play safe and generate as we don't know if source has changed or not
                    if (configuration.isDefaultOverwrite()) {
                        generate = true;
                    } else {
                        generate = generatedFile.getLastModified() <= 0;
                    }
                }

                if (generate) {
                    if (elements == null){
                        elements = new HashSet<TypeElement>();
                    }
                    for (Property property : model.getProperties()){
                        if (property.getType().getCategory() == TypeCategory.CUSTOM){
                            Set<TypeElement> customElements = typeElements.get(property.getType().getFullName());
                            if (customElements != null){
                                elements.addAll(customElements);
                            }
                        }
                    }

                    msg.printMessage(Kind.NOTE, "Generating " + className + " for " + elements);
                    JavaFileObject fileObject = env.getFiler().createSourceFile(className, elements.toArray(new Element[elements.size()]));
                    Writer writer = fileObject.openWriter();
                    try {
                        SerializerConfig serializerConfig = configuration.getSerializerConfig(model);
                        serializer.serialize(model, serializerConfig, new JavaWriter(writer));
                    }finally{
                        if (writer != null) {
                            writer.close();
                        }
                    }
                }else{
                    msg.printMessage(Kind.NOTE, className + " is up-to-date ");
                }

            } catch (IOException e) {
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

    private void serializeVariableList(String packageName, Variables vars, List<EntityType> models){
        String className = packageName + "." + vars.value();
        TypeMappings typeMappings = configuration.getTypeMappings();
        try{
            JavaFileObject fileObject = env.getFiler().createSourceFile(className);
            Writer w = fileObject.openWriter();
            try{
                JavaWriter writer = new JavaWriter(w);
                writer.packageDecl(packageName);
                Type simpleType = new SimpleType(packageName + "." + vars.value(), packageName, vars.value());
                if (vars.asInterface()){
                    writer.beginInterface(simpleType);
                }else{
                    writer.beginClass(simpleType, null);
                }
                for (EntityType model : models){
                    Type queryType = typeMappings.getPathType(model, model, true);
                    String simpleName = model.getUncapSimpleName();
                    String alias = simpleName;
                    if (configuration.getKeywords().contains(simpleName.toUpperCase())){
                        alias += "1";
                    }
                    writer.publicStaticFinal(queryType, simpleName, "new " + queryType.getSimpleName() + "(\"" + alias + "\")");
                }
                writer.end();
            }finally{
                w.close();
            }
        } catch (IOException e) {
            env.getMessager().printMessage(Kind.ERROR, e.getMessage());
        }

    }

}
