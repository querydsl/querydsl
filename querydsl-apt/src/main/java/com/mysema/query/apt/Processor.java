/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

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

    private final Map<String,Set<Element>> typeElements = new HashMap<String,Set<Element>>();
    
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

    private final APTTypeFactory typeModelFactory;

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
        this.typeModelFactory = new APTTypeFactory(env, configuration, factory, anns);
        this.elementHandler = new ElementHandler(configuration, typeModelFactory);

    }
    
    private void addElement(String entityName, Element element){
        Set<Element> elements = typeElements.get(entityName);
        if (elements == null){
            elements = new HashSet<Element>();
            typeElements.put(entityName, elements);
        }
        elements.add(element);
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

    private void handleExtensionType(TypeMirror type, Element element) {
        EntityType entityModel = typeModelFactory.createEntityType(type);
        addElement(entityModel.getFullName(), element);
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
        extensionTypes.put(entityModel.getFullName(), entityModel);

    }

    public void process() {
        processAnnotations();

        // remove entity types from extensionTypes
        for (String key : entityTypes.keySet()){
            extensionTypes.remove(key);
        }

        serializeModels();

        // serialize variable classes
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

    private void serializeModels() {
        // serialize models
        Messager msg = env.getMessager();
        if (!actualSupertypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "Serializing Supertypes");
            serialize(configuration.getSupertypeSerializer(), actualSupertypes.values());
        }
        if (!entityTypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "Serializing Entity types");
            serialize(configuration.getEntitySerializer(), entityTypes.values());
        }
        if (!extensionTypes.isEmpty()){
            msg.printMessage(Kind.NOTE, "Serializing Extension types");
            serialize(configuration.getEmbeddableSerializer(), extensionTypes.values());
        }
        if (!embeddables.isEmpty()){
            msg.printMessage(Kind.NOTE, "Serializing Embeddable types");
            serialize(configuration.getEmbeddableSerializer(), embeddables.values());
        }
        if (!dtos.isEmpty()){
            msg.printMessage(Kind.NOTE, "Serializing DTO types");
            serialize(configuration.getDTOSerializer(), dtos.values());
        }
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
    
    @SuppressWarnings("unchecked")
    private void processEntitiesFromPackage(){
        Class<? extends Annotation> annotation = configuration.getEntitiesAnnotation();
        List<TypeMirror> mirrors = new ArrayList<TypeMirror>();
        
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)){
            for (AnnotationMirror mirror : element.getAnnotationMirrors()){
                if (mirror.getAnnotationType().asElement().getSimpleName().toString().equals(QueryEntities.class.getSimpleName())){
                    for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()){
                        if (entry.getKey().getSimpleName().toString().equals("value")){
                            List<AnnotationValue> values = (List<AnnotationValue>) entry.getValue().getValue();
                            for (AnnotationValue value : values){
                                TypeMirror type = (TypeMirror) value.getValue();                                
                                typeModelFactory.createEntityType(type);
                                mirrors.add(type);
                            }
                        }
                    }
                }
            }
        }
        
        Map<String,EntityType> types = entityTypes;
        Deque<Type> superTypes = new ArrayDeque<Type>();
        
        // get annotated types
        for (TypeMirror mirror : mirrors){
            TypeElement element = (TypeElement) env.getTypeUtils().asElement(mirror);
            EntityType model = elementHandler.handleNormalType(element);
            addElement(model.getFullName(), element);
            types.put(model.getFullName(), model);
            if (model.getSuperType() != null){
                superTypes.push(model.getSuperType().getType());
            }
        }
        
        mergeTypes(types, superTypes);
    }

    private void process(Class<? extends Annotation> annotation, Map<String,EntityType> types){
        Deque<Type> superTypes = new ArrayDeque<Type>();

        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnnotation() == null
                    || element.getAnnotation(configuration.getEmbeddableAnnotation()) == null){
                typeModelFactory.createEntityType(element.asType());
            }
        }

        // get annotated types
        for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
            if (configuration.getEmbeddableAnnotation() == null
                    || element.getAnnotation(configuration.getEmbeddableAnnotation()) == null){
                EntityType model = elementHandler.handleNormalType((TypeElement) element);
                addElement(model.getFullName(), element);
                types.put(model.getFullName(), model);
                if (model.getSuperType() != null){
                    superTypes.push(model.getSuperType().getType());
                }
            }
        }

        mergeTypes(types, superTypes);
    }

    private void mergeTypes(Map<String, EntityType> types, Deque<Type> superTypes) {
        // get external supertypes
        while (!superTypes.isEmpty()){
            Type superType = superTypes.pop();
            if (!types.containsKey(superType.getFullName())  && !allSupertypes.containsKey(superType.getFullName())){
                TypeElement typeElement = env.getElementUtils().getTypeElement(superType.getFullName());
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

    private void processCustomTypes() {
        for (Element queryMethod : roundEnv.getElementsAnnotatedWith(QueryMethod.class)){
            Element element = queryMethod.getEnclosingElement();
            if (element.getAnnotation(QueryExtensions.class) != null){
                continue;
            }else if (element.getAnnotation(configuration.getEntityAnnotation()) != null){
                continue;
            }else if (configuration.getSuperTypeAnnotation() != null && element.getAnnotation(configuration.getSuperTypeAnnotation()) != null){
                continue;
            }else if (configuration.getEmbeddableAnnotation() != null && element.getAnnotation(configuration.getEmbeddableAnnotation()) != null){
                continue;
            }
            handleExtensionType(element.asType(), element);
        }
    }

    private void processDelegateMethods(){
        Set<EntityType> types = new HashSet<EntityType>();
        for (Element delegateMethod : roundEnv.getElementsAnnotatedWith(QueryDelegate.class)){
            ExecutableElement method = (ExecutableElement)delegateMethod;
            Element element = delegateMethod.getEnclosingElement();
            String name = method.getSimpleName().toString();
            Type delegateType = typeModelFactory.create(element.asType());
            Type returnType = typeModelFactory.create(method.getReturnType());
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
                                entityType = typeModelFactory.createEntityType(type);
                            }
                        }
                    }
                }
            }

            if (entityType != null){
                addElement(entityType.getFullName(), element);
                entityType.addDelegate(new Delegate(entityType, delegateType, name, parameters, returnType));
                types.add(entityType);
            }            
        }
        
        for (EntityType type : types){
            if (type.getOriginalCategory() != TypeCategory.SIMPLE){
                extensionTypes.put(type.getFullName(), type);
            }            
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
                addElement(model.getFullName(), element);
                dtos.put(model.getFullName(), model);
                visitedDTOTypes.add(parent);

            }
        }
    }

    private void processEmbeddables() {
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnnotation())) {
            typeModelFactory.createEntityType(element.asType());
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddableAnnotation())) {
            EntityType model = elementHandler.handleNormalType((TypeElement) element);
            addElement(model.getFullName(), element);
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
        for (Element element : roundEnv.getElementsAnnotatedWith(configuration.getEmbeddedAnnotation())) {
            TypeMirror type = element.asType();
            if (element.getKind() == ElementKind.METHOD){
                type = ((ExecutableElement)element).getReturnType();
            }
            String typeName = type.toString();
            // TODO : make this safer
            if (typeName.startsWith("java.util")){
                typeName = typeName.substring(typeName.indexOf('<')+1, typeName.lastIndexOf('>'));
                typeModelFactory.createEntityType(env.getElementUtils().getTypeElement(typeName).asType());
            }
            TypeElement typeElement = env.getElementUtils().getTypeElement(typeName);
            EntityType model = elementHandler.handleNormalType(typeElement);
            addElement(model.getFullName(), element);
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

    private void processExtensions() {
        for (Element element : roundEnv.getElementsAnnotatedWith(QueryExtensions.class)){
            for (AnnotationMirror annotation : element.getAnnotationMirrors()){
                if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(QueryExtensions.class.getSimpleName())){
                    for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : annotation.getElementValues().entrySet()){
                        if (entry.getKey().getSimpleName().toString().equals("value")){
                            if (entry.getValue().getValue() instanceof TypeMirror){
                                TypeMirror type = (TypeMirror)entry.getValue().getValue();
                                handleExtensionType(type, element);
                            }
                        }
                    }
                }
            }
        }
    }

    private void processSupertypes() {
        process(configuration.getSuperTypeAnnotation(), actualSupertypes);
    }

    private void serialize(Serializer serializer, Collection<EntityType> models) {
        Messager msg = env.getMessager();
        for (EntityType model : models) {
            try {
                String packageName = model.getPackageName();
                Type type = configuration.getTypeMappings().getPathType(model, model, true);
                String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();

                Filer filer = env.getFiler();

                FileObject sourceFile = getSourceFile(model.getFullName());
                FileObject generatedFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, type.getSimpleName() + ".java");
                
                boolean generate;
                // Source file is accessible and exists
                if (sourceFile != null && sourceFile.getLastModified() > 0) {
                    // Generate if source has changed since Q-type was last time generated
                    generate = generatedFile.getLastModified() <= sourceFile.getLastModified();
                } else {
                    // Play safe and generate as we don't know if source has changed or not
                    if (configuration.isDefaultOverwrite()) {
                        generate = true;
                    } else {
                        generate = generatedFile.getLastModified() <= 0;
                    }
                }

                if (generate) {
                    Set<Element> elements = typeElements.get(model.getFullName());
                    if (elements == null){
                        elements = new HashSet<Element>();
                    }
                    for (Property property : model.getProperties()){
                        if (property.getType().getCategory() == TypeCategory.CUSTOM){
                            Set<Element> customElements = typeElements.get(property.getType().getFullName());
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
