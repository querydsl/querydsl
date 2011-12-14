/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.apt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryExclude;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.codegen.Delegate;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.QueryTypeFactory;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.TypeMappings;

/**
 * Processor handles the actual work in the Querydsl APT module
 *
 * @author tiwe
 *
 */
@Deprecated
class Processor {

//    /**
//     * Cache for annotated elements
//     */
//    static final Map<Class<? extends Annotation>, Set<Element>> elementCache = Collections.synchronizedMap(
//            new HashMap<Class<? extends Annotation>,Set<Element>>());

    /**
     * Mapping of entity types to TypeElements which contribute to the generated class
     */
    private final Map<String, Set<TypeElement>> typeElements = new HashMap<String,Set<TypeElement>>();

    private final Set<Class<? extends Annotation>> entityAnnotations = new HashSet<Class<? extends Annotation>>();
    
    private final Map<String, EntityType> actualSupertypes  = new HashMap<String, EntityType>();

    private final Map<String, EntityType> allSupertypes = new HashMap<String, EntityType>();

    private final Configuration configuration;

    private final Map<String, EntityType> projectionTypes = new HashMap<String, EntityType>();

    private final TypeElementHandler elementHandler;

    private final Map<String, EntityType> embeddables = new HashMap<String,EntityType>();

    private final Map<String, EntityType> entityTypes = new HashMap<String, EntityType>();

    private final ProcessingEnvironment env;

    private final Map<String, EntityType> extensionTypes = new HashMap<String,EntityType>();

    private final RoundEnvironment roundEnv;

    private final ExtendedTypeFactory typeFactory;

    private final TypeExtractor typeExtractor = new TypeExtractor(true);
    
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

        entityAnnotations.add(configuration.getEntityAnnotation());
        if (configuration.getSuperTypeAnnotation() != null) {
            entityAnnotations.add(configuration.getSuperTypeAnnotation());
        }
        if (configuration.getEmbeddableAnnotation() != null) {
            entityAnnotations.add(configuration.getEmbeddableAnnotation());
        }

        TypeMappings typeMappings = configuration.getTypeMappings();
        QueryTypeFactory queryTypeFactory = configuration.getQueryTypeFactory();
        this.typeFactory = new ExtendedTypeFactory(env, configuration, entityAnnotations, typeMappings, queryTypeFactory);
        this.elementHandler = new TypeElementHandler(configuration, typeFactory, typeMappings, queryTypeFactory);
    }

    public void process() {
        processAnnotations();

        for (String key : actualSupertypes.keySet()) {
            entityTypes.remove(key);
            extensionTypes.remove(key);
            embeddables.remove(key);
        }
        
        for (String key : entityTypes.keySet()) {
            actualSupertypes.remove(key);
            extensionTypes.remove(key);
            embeddables.remove(key);
        }

        serializeTypes();

//        serializeVariableClasses();
    }

    private void processAnnotations() {
        processExclusions();        
        
        processDelegateMethods();
        
        if (configuration.isUnknownAsEmbedded()) {
            env.getMessager().printMessage(Kind.NOTE, "Collecting custom types");
            processFromProperties();
        }
        
        boolean hasEmbeddableAnnotation = configuration.getEmbeddableAnnotation() != null;
        
        if (configuration.getSuperTypeAnnotation() != null) {
            process(configuration.getSuperTypeAnnotation(), hasEmbeddableAnnotation, actualSupertypes);
        }

        if (configuration.getEmbeddedAnnotation() != null) {
            processEmbedded();
        }

        if (configuration.getEmbeddableAnnotation() != null) {
            process(configuration.getEmbeddableAnnotation(), false, embeddables);
        }

        if (configuration.getEntitiesAnnotation() != null) {
            processEntitiesFromPackage();
        }

        process(configuration.getEntityAnnotation(), hasEmbeddableAnnotation, entityTypes);

        processProjectionTypes();
        
    }
    
    /**
     * Get the elements with the given annotation type
     * 
     * @param a
     * @return
     */
    private Set<? extends Element> getElements(Class<? extends Annotation> a) {
        return roundEnv.getElementsAnnotatedWith(a);
    }

    private void processExclusions() {
        for (Element element : getElements(QueryExclude.class)) {
            if (element instanceof PackageElement) {
                configuration.addExcludedPackage(((PackageElement)element).getQualifiedName().toString());
            } else if (element instanceof TypeElement) {
                configuration.addExcludedClass(((TypeElement)element).getQualifiedName().toString());
            } else {
                throw new IllegalArgumentException(element.toString());
            }
        }
    }
       
    private void process(Class<? extends Annotation> annotation, boolean skipEmbeddables, Map<String, EntityType> types) {
        process(getElements(annotation), skipEmbeddables, types);
    }
    
    @SuppressWarnings("unchecked")
    private void process(Set<? extends Element> elements, boolean skipEmbeddables, Map<String, EntityType> types) {
        Deque<Type> superTypes = new ArrayDeque<Type>();
        List<TypeElement> allElements = new ArrayList<TypeElement>();
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>();
        List<TypeMirror> superTypeMirrors = new ArrayList<TypeMirror>();
        
        for (TypeElement element : (Set<TypeElement>)elements) {            
            if (skipEmbeddables && element.getAnnotation(configuration.getEmbeddableAnnotation()) != null) {
                continue;
            }
            typeFactory.getEntityType(element.asType(), false);
            typeMirrors.add(element.asType());
            allElements.add(element);
                
            addAnnotationlessSupertypes(superTypeMirrors, allElements, element);
        }

        // super type handling
        for (TypeMirror typeMirror : superTypeMirrors) {
            typeFactory.getEntityType(typeMirror, true);
        }
        
        for (TypeMirror typeMirror : typeMirrors) {
            typeFactory.getEntityType(typeMirror, true);
        }

        // get annotated types
        for (TypeElement element : allElements) {
            EntityType model = elementHandler.handleEntityType(element);
            registerTypeElement(model.getFullName(), element);
            types.put(model.getFullName(), model);
            if (model.getSuperType() != null) {
                superTypes.push(model.getSuperType().getType());
            }
        }

        mergeTypes(types, superTypes);
    }
    
    private void processEmbedded() {
        Set<Element> elements = new HashSet<Element>();

        // only creation
        for (Element element : getElements(configuration.getEmbeddedAnnotation())) {
            TypeMirror type = element.asType();
            if (element.getKind() == ElementKind.METHOD){
                type = ((ExecutableElement)element).getReturnType();
            }
            String typeName = type.toString();
            if (typeName.startsWith(Collection.class.getName())
             || typeName.startsWith(List.class.getName())
             || typeName.startsWith(Set.class.getName())) {
                type = ((DeclaredType)type).getTypeArguments().get(0);
                
            } else if (typeName.startsWith(Map.class.getName())){
                type = ((DeclaredType)type).getTypeArguments().get(1);
            }
            
            TypeElement typeElement = typeExtractor.visit(type);
            if (typeElement != null && typeElement.getAnnotation(configuration.getEntityAnnotation()) == null) {
                elements.add(typeElement);
            }
        }

        process(elements, false, embeddables);                
    }      
     
    @SuppressWarnings("unchecked")
    private void processEntitiesFromPackage() {
        Class<? extends Annotation> annotation = configuration.getEntitiesAnnotation();
        Set<Element> elements = new HashSet<Element>();

        // only creation
        for (Element element : getElements(annotation)) {
            for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
                if (TypeUtils.isAnnotationMirrorOfType(mirror, annotation)) {
                    for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
                        if (entry.getKey().getSimpleName().toString().equals("value")) {
                            List<AnnotationValue> values = (List<AnnotationValue>) entry.getValue().getValue();
                            for (AnnotationValue value : values) {
                                DeclaredType type = (DeclaredType) value.getValue();
                                elements.add(type.asElement());
                            }
                        }
                    }
                }
            }
        }

        process(elements, false, entityTypes);
    }
    
    private void processFromProperties() {
        Set<Element> elements = new HashSet<Element>();
        for (Class<? extends Annotation> annotation : entityAnnotations) {
            elements.addAll(getElements(annotation));    
        }
        
        Set<TypeElement> types = new HashSet<TypeElement>();
        
        // classes
        for (Element element : elements) {
            if (element instanceof TypeElement) {
                processFromProperties((TypeElement)element, types);                
            }
        }
        
        List<TypeMirror> typeMirrors = new ArrayList<TypeMirror>(types.size());
        
        for (TypeElement type : types) {
            // skip internal types
            if (type.getQualifiedName().toString().startsWith("java.")) {
                continue;
            }
            
            // skip annotated
            boolean annotated = false;
            for (Class<? extends Annotation> annotation : entityAnnotations) {
                annotated |= type.getAnnotation(annotation) != null;
            }
            if (annotated) {
                continue;
            }
            
            typeFactory.getEntityType(type.asType(), false);
            typeMirrors.add(type.asType());
        }
        
        for (TypeMirror typeMirror : typeMirrors) {
            typeFactory.getEntityType(typeMirror, true);
        }

        for (TypeElement element : types) {
            if (typeMirrors.contains(element.asType())){
                EntityType model = elementHandler.handleEntityType(element);
                registerTypeElement(model.getFullName(), element);
                embeddables.put(model.getFullName(), model);    
            }            
        }
        
        if (configuration.getEmbeddableAnnotation() == null) {
            allSupertypes.putAll(embeddables);

            // add super type fields
            Set<EntityType> handled = new HashSet<EntityType>();
            for (EntityType embeddable : embeddables.values()) {
                addSupertypeFields(embeddable, allSupertypes, handled);
            }    
        }        
    }
   
    private void processFromProperties(TypeElement type, Set<TypeElement> types) {
        List<? extends Element> children = type.getEnclosedElements();
        VisitorConfig config = configuration.getConfig(type, children);
        
        // fields
        if (config.visitFieldProperties()) {
            for (VariableElement field : ElementFilter.fieldsIn(children)) {
                TypeElement typeElement = typeExtractor.visit(field.asType());
                if (typeElement != null) {
                    types.add(typeElement);
                }
            }
        }
        
        // getters
        if (config.visitMethodProperties()) {
            for (ExecutableElement method : ElementFilter.methodsIn(children)) {
                String name = method.getSimpleName().toString();
                if ((name.startsWith("get") || name.startsWith("is")) && method.getParameters().isEmpty()) {
                    TypeElement typeElement = typeExtractor.visit(method.getReturnType());
                    if (typeElement != null) {
                        types.add(typeElement);
                    }    
                }                                       
            }
        }
    }

    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes, Set<EntityType> handled) {
        if (handled.add(model)) {
            for (Supertype supertype : model.getSuperTypes()) {
                EntityType entityType = superTypes.get(supertype.getType().getFullName());
                if (entityType != null) {
                    addSupertypeFields(entityType, superTypes, handled);
                    supertype.setEntityType(entityType);
                    model.include(supertype);
                }
            }
        }
    }

//    @SuppressWarnings("unchecked")
//    private Set<Element> getElementsAndCache(Class<? extends Annotation> annotationType) {
//        Set<Element> elements = (Set<Element>)getElements(annotationType);
//        if (!elements.isEmpty()) {
//            Set<Element> cached = elementCache.get(annotationType);
//            if (cached == null) {
//                cached = new HashSet<Element>();
//                elementCache.put(annotationType, cached);
//            }
//            cached.addAll(elements);
//        }
//        return elements;
//    }

//    private Set<Element> getElementsFromCache(Class<? extends Annotation> annotationType) {
//        Set<Element> cached = elementCache.get(annotationType);
//        if (cached != null) {
//            Set<Element> cloned = new HashSet<Element>();
//            for (Element element : cached) {
//                // clone type element
//                if (element instanceof TypeElement) {
//                    cloned.add(env.getElementUtils().getTypeElement(((TypeElement) element).getQualifiedName().toString()));
//                // cloned other elements via parent
//                } else {
//                    Element parent = element.getEnclosingElement();
//                    if (parent instanceof TypeElement) {
//                        parent = env.getElementUtils().getTypeElement(((TypeElement) parent).getQualifiedName().toString());
//                        for (Element child : parent.getEnclosedElements()) {
//                            // TODO : better equals check
//                            if (child.getKind() == element.getKind() && child.getSimpleName().equals(element.getSimpleName())) {
//                                cloned.add(child);
//                            }
//                        }
//                    } else {
//                        env.getMessager().printMessage(Kind.WARNING, element + " from cache");
//                        cloned.add(element);
//                    }
//                }
//            }
//            return cloned;
//        } else {
//            return Collections.emptySet();
//        }
//    }
  
    private void mergeTypes(Map<String, EntityType> types, Deque<Type> superTypes) {
        // get external supertypes
        while (!superTypes.isEmpty()) {
            Type superType = superTypes.pop();
            if (!types.containsKey(superType.getFullName()) && !allSupertypes.containsKey(superType.getFullName())) {
                TypeElement typeElement = env.getElementUtils().getTypeElement(superType.getFullName());
                if (typeElement == null) {
                    throw new IllegalStateException("Found no type for " + superType.getFullName());
                }
                EntityType entityType = elementHandler.handleEntityType(typeElement);
                if (entityType.getSuperType() != null) {
                    superTypes.push(entityType.getSuperType().getType());
                }
                allSupertypes.put(superType.getFullName(), entityType);
            }
        }

        allSupertypes.putAll(types);

        // add supertype fields
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType type : types.values()) {
            addSupertypeFields(type, allSupertypes, handled);
        }
    }

    
    private boolean hasKnownAnnotation(Element element) {
        if (configuration.getEmbeddableAnnotation() != null && element.getAnnotation(configuration.getEmbeddableAnnotation()) != null) {
            return true;
        } else if (configuration.getSuperTypeAnnotation() != null && element.getAnnotation(configuration.getSuperTypeAnnotation()) != null) {
            return true;
        } else if (element.getAnnotation(configuration.getEntityAnnotation()) != null) {
            return true;
        } else {
            return false;    
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
        for (AnnotationMirror annotation : delegateMethod.getAnnotationMirrors()) {
            if (TypeUtils.isAnnotationMirrorOfType(annotation, QueryDelegate.class)) {
                for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                    if (entry.getKey().getSimpleName().toString().equals("value")) {
                        if (entry.getValue().getValue() instanceof TypeMirror) {
                            TypeMirror type = (TypeMirror)entry.getValue().getValue();
                            entityType = typeFactory.getEntityType(type, true);
                        }
                    }
                }
            }
        }

        if (entityType != null) {
            registerTypeElement(entityType.getFullName(), (TypeElement)element);
            entityType.addDelegate(new Delegate(entityType, delegateType, name, parameters, returnType));
            if (!cached) {
                extensionTypes.put(entityType.getFullName(), entityType);
            }
        }
    }

    private void processDelegateMethods() {
        //  get elements from cache
//        for (Element delegateMethod : getElementsFromCache(QueryDelegate.class)) {
        for (Element delegateMethod : getElements(QueryDelegate.class)) {
            processDelegateMethod(delegateMethod, true);
        }

        // get elements from roundEnv
//        for (Element delegateMethod : getElementsAndCache(QueryDelegate.class)) {
        for (Element delegateMethod : getElements(QueryDelegate.class)) {
            processDelegateMethod(delegateMethod, false);
        }
    }

    private void processProjectionTypes() {
        Set<Element> visitedDTOTypes = new HashSet<Element>();
        for (Element element : getElements(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (parent.getAnnotation(configuration.getEntityAnnotation()) == null
                    && parent.getAnnotation(configuration.getEmbeddableAnnotation()) == null
                    && !visitedDTOTypes.contains(parent)) {
                EntityType model = elementHandler.handleProjectionType((TypeElement)parent);
                registerTypeElement(model.getFullName(), (TypeElement)parent);
                projectionTypes.put(model.getFullName(), model);
                visitedDTOTypes.add(parent);
            }
        }
    }
 
    private void addAnnotationlessSupertypes(List<TypeMirror> superTypeMirrors,
            List<TypeElement> elements, TypeElement element) {
        // add annotationless supertype
        TypeMirror superTypeMirror = element.getSuperclass();
        while (superTypeMirror != null){
            TypeElement superTypeElement = (TypeElement) env.getTypeUtils().asElement(superTypeMirror);
            if (superTypeElement != null 
                    && !superTypeElement.toString().startsWith("java.lang.") 
                    && !hasKnownAnnotation(superTypeElement)) {
                typeFactory.getEntityType(superTypeMirror, false);
                superTypeMirrors.add(superTypeMirror);
                elements.add(superTypeElement);
                superTypeMirror = superTypeElement.getSuperclass();
                if (superTypeMirror instanceof NoType) {
                    superTypeMirror = null;
                }
            } else {
                superTypeMirror = null;
            }
        }
    }  

    private void registerTypeElement(String entityName, TypeElement element) {
        Set<TypeElement> elements = typeElements.get(entityName);
        if (elements == null) {
            elements = new HashSet<TypeElement>();
            typeElements.put(entityName, elements);
        }
        elements.add(element);
    }
    
    // SERIALIZATION
    
    private void serializeTypes() {
        if (!actualSupertypes.isEmpty()) {
            env.getMessager().printMessage(Kind.NOTE, "Serializing Supertypes");
            serialize(configuration.getSupertypeSerializer(), actualSupertypes.values());
        }

        if (!entityTypes.isEmpty()) {
            env.getMessager().printMessage(Kind.NOTE, "Serializing Entity types");
            serialize(configuration.getEntitySerializer(), entityTypes.values());
        }

        if (!extensionTypes.isEmpty()) {
            env.getMessager().printMessage(Kind.NOTE, "Serializing Extension types");
            serialize(configuration.getEmbeddableSerializer(), extensionTypes.values());
        }

        if (!embeddables.isEmpty()) {
            env.getMessager().printMessage(Kind.NOTE, "Serializing Embeddable types");
            serialize(configuration.getEmbeddableSerializer(), embeddables.values());
        }

        if (!projectionTypes.isEmpty()) {
            env.getMessager().printMessage(Kind.NOTE, "Serializing Projection types");
            serialize(configuration.getDTOSerializer(), projectionTypes.values());
        }

    }

//    private void serializeVariableClasses() {
//        for (Element element : getElements(Variables.class)) {
//            if (element instanceof PackageElement) {
//                Variables vars = element.getAnnotation(Variables.class);
//                PackageElement packageElement = (PackageElement)element;
//                List<EntityType> models = new ArrayList<EntityType>();
//                for (EntityType model : entityTypes.values()) {
//                    if (model.getPackageName().equals(packageElement.getQualifiedName().toString())) {
//                        models.add(model);
//                    }
//                }
//                serializeVariableList(packageElement.getQualifiedName().toString(), vars, models);
//            }
//        }
//    }

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

    private void serialize(Serializer serializer, Collection<EntityType> models) {
        Messager msg = env.getMessager();
        Filer filer = env.getFiler();
        for (EntityType model : models) {            
            try {
                Type type = configuration.getTypeMappings().getPathType(model, model, true);
                String packageName = type.getPackageName();
                String className = !packageName.isEmpty() ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
                
                // skip if type is excluded class or in excluded package
                if (configuration.isExcludedPackage(model.getPackageName()) || configuration.isExcludedClass(model.getFullName())) {
                    continue;
                }                
                
                Set<TypeElement> elements = typeElements.get(model.getFullName());
                
                if (isGenerate(type, filer, elements)) {
                    if (elements == null) {
                        elements = new HashSet<TypeElement>();
                    }
                    for (Property property : model.getProperties()) {
                        if (property.getType().getCategory() == TypeCategory.CUSTOM) {
                            Set<TypeElement> customElements = typeElements.get(property.getType().getFullName());
                            if (customElements != null) {
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
                    } finally {
                        if (writer != null) {
                            writer.close();
                        }
                    }
                } else {
                    msg.printMessage(Kind.NOTE, className + " is up-to-date ");
                }
                
            } catch (IOException e) {
                e.printStackTrace();
                msg.printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

    protected boolean isGenerate(Type type, Filer filer, Set<TypeElement> elements) throws IOException {
        String packageName = type.getPackageName();
        boolean generate = false;
        try {
            FileObject generatedFile = filer.getResource(StandardLocation.SOURCE_OUTPUT, packageName, type.getSimpleName() + ".java");                    
            if (elements != null){
                boolean foundSources = false;
                for (TypeElement element : elements) {
                    FileObject sourceFile = getSourceFile(element.getQualifiedName().toString());
                    // Source file is accessible and exists
                    if (sourceFile != null && sourceFile.getLastModified() > 0) {
                        foundSources = true;
                        // Generate if source has changed since Q-type was last time generated
                        generate |= generatedFile.getLastModified() <= sourceFile.getLastModified();
                    }
                }
                if (!foundSources) {
//                    if (configuration.isDefaultOverwrite()) {
                        generate = true;
//                    } else {
//                        generate = generatedFile.getLastModified() <= 0;
//                    }
                }

            } else {
                // Play safe and generate as we don't know if source has changed or not
//                if (configuration.isDefaultOverwrite()) {
                    generate = true;
//                } else {
//                    generate = generatedFile.getLastModified() <= 0;
//                }
            }
        } catch( FileNotFoundException e) {
            generate = true;
        }
        return generate;
    }

//    private void serializeVariableList(String packageName, Variables vars, List<EntityType> models) {
//        String className = packageName + "." + vars.value();
//        TypeMappings typeMappings = configuration.getTypeMappings();
//        try{
//            JavaFileObject fileObject = env.getFiler().createSourceFile(className);
//            Writer w = fileObject.openWriter();
//            try{
//                JavaWriter writer = new JavaWriter(w);
//                writer.packageDecl(packageName);
//                Type simpleType = new SimpleType(packageName + "." + vars.value(), packageName, vars.value());
//                if (vars.asInterface()) {
//                    writer.beginInterface(simpleType);
//                } else {
//                    writer.beginClass(simpleType, null);
//                }
//                for (EntityType model : models) {
//                    Type queryType = typeMappings.getPathType(model, model, true);
//                    String simpleName = model.getUncapSimpleName();
//                    String alias = simpleName;
//                    if (configuration.getKeywords().contains(simpleName.toUpperCase())) {
//                        alias += "1";
//                    }
//                    writer.publicStaticFinal(queryType, simpleName, "new " + queryType.getSimpleName() + "(\"" + alias + "\")");
//                }
//                writer.end();
//            } finally {
//                w.close();
//            }
//        } catch (IOException e) {            
//            env.getMessager().printMessage(Kind.ERROR, e.getMessage());
//        }
//
//    }

}
