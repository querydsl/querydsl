package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
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
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
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
 * @author tiwe
 *
 */
public abstract class AbstractQuerydslProcessor extends AbstractProcessor {
    
    public static final Boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = Boolean.FALSE;
    
    private final TypeExtractor typeExtractor = new TypeExtractor(true);
    
    private Configuration configuration;
    
    private RoundEnvironment roundEnv;
    
    private ExtendedTypeFactory typeFactory;
    
    private TypeElementHandler elementHandler;
    
    private Context context;
        
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {    
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
        configuration = createConfiguration(roundEnv);
        context = new Context();        
        Set<Class<? extends Annotation>> entityAnnotations = configuration.getEntityAnnotations();        
        TypeMappings typeMappings = configuration.getTypeMappings();
        QueryTypeFactory queryTypeFactory = configuration.getQueryTypeFactory();
        this.typeFactory = new ExtendedTypeFactory(processingEnv, configuration, entityAnnotations, typeMappings, queryTypeFactory);
        elementHandler = new TypeElementHandler(configuration, typeFactory, typeMappings, queryTypeFactory);
        this.roundEnv = roundEnv;
       
        processAnnotations();        
        context.clean();        
        serializeTypes();           
        return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }
    
    private void processAnnotations() {
//      get supertype elements
//      get embeddable elements
//      get embedded elements        
//      get entity annotations        
//      get entity elements
//      get projection elements
                
//      create meta models
//      add meta model parents
//      add properties to meta models
        
        processExclusions();        
        
        processDelegateMethods();
        
        if (configuration.isUnknownAsEmbedded()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Collecting custom types");
            processFromProperties();
        }
        
        boolean hasEmbeddableAnnotation = configuration.getEmbeddableAnnotation() != null;
        
        if (configuration.getSuperTypeAnnotation() != null) {
            process(configuration.getSuperTypeAnnotation(), hasEmbeddableAnnotation, context.actualSupertypes);
        }

        if (configuration.getEmbeddedAnnotation() != null) {
            processEmbedded();
        }

        if (configuration.getEmbeddableAnnotation() != null) {
            process(configuration.getEmbeddableAnnotation(), false, context.embeddables);
        }

        if (configuration.getEntitiesAnnotation() != null) {
            processEntitiesFromPackage();
        }

        process(configuration.getEntityAnnotation(), hasEmbeddableAnnotation, context.entityTypes);

        processProjectionTypes();
        
    }
    
    private void registerTypeElement(String entityName, TypeElement element) {
        Set<TypeElement> elements = context.typeElements.get(entityName);
        if (elements == null) {
            elements = new HashSet<TypeElement>();
            context.typeElements.put(entityName, elements);
        }
        elements.add(element);
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
                context.projectionTypes.put(model.getFullName(), model);
                visitedDTOTypes.add(parent);
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
    
    private void mergeTypes(Map<String, EntityType> types, Deque<Type> superTypes) {
        // get external supertypes
        while (!superTypes.isEmpty()) {
            Type superType = superTypes.pop();
            if (!types.containsKey(superType.getFullName()) && !context.allSupertypes.containsKey(superType.getFullName())) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(superType.getFullName());
                if (typeElement == null) {
                    throw new IllegalStateException("Found no type for " + superType.getFullName());
                }
                EntityType entityType = elementHandler.handleEntityType(typeElement);
                if (entityType.getSuperType() != null) {
                    superTypes.push(entityType.getSuperType().getType());
                }
                context.allSupertypes.put(superType.getFullName(), entityType);
            }
        }

        context.allSupertypes.putAll(types);

        // add supertype fields
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType type : types.values()) {
            addSupertypeFields(type, context.allSupertypes, handled);
        }
    }
    
    private void addAnnotationlessSupertypes(List<TypeMirror> superTypeMirrors,
            List<TypeElement> elements, TypeElement element) {
        // add annotationless supertype
        TypeMirror superTypeMirror = element.getSuperclass();
        while (superTypeMirror != null){
            TypeElement superTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(superTypeMirror);
            if (superTypeElement != null 
                    && !superTypeElement.toString().startsWith("java.lang.") 
                    && !TypeUtils.hasAnnotationOfType(superTypeElement, configuration.getEntityAnnotations())) {
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

        process(elements, false, context.embeddables);                
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

        process(elements, false, context.entityTypes);
    }
    
    private void processFromProperties() {
        Set<Element> elements = new HashSet<Element>();
        for (Class<? extends Annotation> annotation : configuration.getEntityAnnotations()) {
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
            for (Class<? extends Annotation> annotation : configuration.getEntityAnnotations()) {
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
                context.embeddables.put(model.getFullName(), model);    
            }            
        }
        
        if (configuration.getEmbeddableAnnotation() == null) {
            context.allSupertypes.putAll(context.embeddables);

            // add super type fields
            Set<EntityType> handled = new HashSet<EntityType>();
            for (EntityType embeddable : context.embeddables.values()) {
                addSupertypeFields(embeddable, context.allSupertypes, handled);
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
    
    private void processDelegateMethods() {
        for (Element delegateMethod : getElements(QueryDelegate.class)) {
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
                context.extensionTypes.put(entityType.getFullName(), entityType);
            }
        }
    }
        
    private void serializeTypes() {
        if (!context.actualSupertypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Supertypes");
            serialize(configuration.getSupertypeSerializer(), context.actualSupertypes.values());
        }

        if (!context.entityTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Entity types");
            serialize(configuration.getEntitySerializer(), context.entityTypes.values());
        }

        if (!context.extensionTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Extension types");
            serialize(configuration.getEmbeddableSerializer(), context.extensionTypes.values());
        }

        if (!context.embeddables.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Embeddable types");
            serialize(configuration.getEmbeddableSerializer(), context.embeddables.values());
        }

        if (!context.projectionTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Projection types");
            serialize(configuration.getDTOSerializer(), context.projectionTypes.values());
        }

    }
    
    private Set<? extends Element> getElements(Class<? extends Annotation> a) {
        return roundEnv.getElementsAnnotatedWith(a);
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
    
    private void serialize(Serializer serializer, Collection<EntityType> models) {
        for (EntityType model : models) {            
            try {
                Type type = configuration.getTypeMappings().getPathType(model, model, true);
                String packageName = type.getPackageName();
                String className = !packageName.isEmpty() ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
                
                // skip if type is excluded class or in excluded package
                if (configuration.isExcludedPackage(model.getPackageName()) || configuration.isExcludedClass(model.getFullName())) {
                    continue;
                }                
                
                Set<TypeElement> elements = context.typeElements.get(model.getFullName());
                
                if (elements == null) {
                    elements = new HashSet<TypeElement>();
                }
                for (Property property : model.getProperties()) {
                    if (property.getType().getCategory() == TypeCategory.CUSTOM) {
                        Set<TypeElement> customElements = context.typeElements.get(property.getType().getFullName());
                        if (customElements != null) {
                            elements.addAll(customElements);
                        }
                    }
                }

                processingEnv.getMessager().printMessage(Kind.NOTE, "Generating " + className + " for " + elements);
                JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(className, elements.toArray(new Element[elements.size()]));
                Writer writer = fileObject.openWriter();
                try {
                    SerializerConfig serializerConfig = configuration.getSerializerConfig(model);
                    serializer.serialize(model, serializerConfig, new JavaWriter(writer));
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
                
            } catch (IOException e) {
                e.printStackTrace();
                processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }

    
    protected abstract Configuration createConfiguration(RoundEnvironment roundEnv);

}
