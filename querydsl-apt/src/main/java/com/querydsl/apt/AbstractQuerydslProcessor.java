/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.apt;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.google.common.collect.Iterables;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.*;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.annotations.QueryExclude;
import com.querydsl.core.annotations.QueryProjection;

/**
 * {@code AbstractQuerydslProcessor} is the base class for Querydsl annotation processors and
 * contains the main processing logic. The subclasses just provide the configuration.
 *
 * @author tiwe
 *
 */
public abstract class AbstractQuerydslProcessor extends AbstractProcessor {

    public static final Boolean ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS = Boolean.FALSE;

    private final TypeExtractor typeExtractor = new TypeExtractor(true);

    private Configuration conf;

    private RoundEnvironment roundEnv;

    private ExtendedTypeFactory typeFactory;

    private TypeElementHandler elementHandler;

    private Context context;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());

        if (roundEnv.processingOver() || annotations.size() == 0) {
            return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
        }

        if (roundEnv.getRootElements() == null || roundEnv.getRootElements().isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "No sources to process");
            return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
        }

        conf = createConfiguration(roundEnv);
        context = new Context();
        Set<Class<? extends Annotation>> entityAnnotations = conf.getEntityAnnotations();
        TypeMappings typeMappings = conf.getTypeMappings();
        QueryTypeFactory queryTypeFactory = conf.getQueryTypeFactory();
        this.typeFactory = new ExtendedTypeFactory(processingEnv, entityAnnotations, typeMappings, queryTypeFactory, conf.getVariableNameFunction());
        elementHandler = new TypeElementHandler(conf, typeFactory, typeMappings, queryTypeFactory);
        this.roundEnv = roundEnv;

        // process annotations
        processAnnotations();

        validateMetaTypes();

        // serialize created types
        serializeMetaTypes();

        return ALLOW_OTHER_PROCESSORS_TO_CLAIM_ANNOTATIONS;
    }

    private void processAnnotations() {
        processExclusions();

        Set<TypeElement> elements = collectElements();

        // create meta models
        for (Element element : elements) {
            typeFactory.getEntityType(element.asType(), false);
        }
        for (Element element : elements) {
            typeFactory.getEntityType(element.asType(), true);
        }

        // add properties
        boolean embeddableAnn = conf.getEmbeddableAnnotation() != null;
        boolean altEntityAnn = conf.getAlternativeEntityAnnotation() != null;
        boolean superAnn = conf.getSuperTypeAnnotation() != null;
        for (TypeElement element : elements) {
            EntityType entityType = elementHandler.handleEntityType(element);
            registerTypeElement(entityType.getFullName(), element);
            if (element.getAnnotation(conf.getEntityAnnotation()) != null) {
                context.entityTypes.put(entityType.getFullName(), entityType);
            } else if (altEntityAnn && element.getAnnotation(conf.getAlternativeEntityAnnotation()) != null) {
                context.entityTypes.put(entityType.getFullName(), entityType);
            } else if (embeddableAnn && element.getAnnotation(conf.getEmbeddableAnnotation()) != null) {
                context.embeddableTypes.put(entityType.getFullName(), entityType);
            } else if (superAnn && element.getAnnotation(conf.getSuperTypeAnnotation()) != null) {
                context.supertypes.put(entityType.getFullName(), entityType);
            } else if (!entityType.getDelegates().isEmpty()) {
                context.extensionTypes.put(entityType.getFullName(), entityType);
            } else {
                context.embeddableTypes.put(entityType.getFullName(), entityType);
            }
            context.allTypes.put(entityType.getFullName(), entityType);
        }

        // track also methods from external entity types
        for (EntityType entityType : new ArrayList<EntityType>(typeFactory.getEntityTypes())) {
            String fullName = entityType.getFullName();
            if (!context.allTypes.keySet().contains(fullName)) {
                TypeElement element = processingEnv.getElementUtils().getTypeElement(fullName);
                if (element != null) {
                    elementHandler.handleEntityType(element);
                }
            }
        }

        // add external parents
        for (Element element : elements) {
            EntityType entityType = typeFactory.getEntityType(element.asType(), false);
            addExternalParents(entityType);
        }

        // add properties from parents
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType entityType : context.allTypes.values()) {
            addSupertypeFields(entityType, handled);
        }

        processProjectionTypes(elements);

        // extend entity types
        typeFactory.extendTypes();

        context.clean();

    }

    private void addExternalParents(EntityType entityType) {
        Deque<Type> superTypes = new ArrayDeque<Type>();
        if (entityType.getSuperType() != null) {
            superTypes.push(entityType.getSuperType().getType());
        }

        while (!superTypes.isEmpty()) {
            Type superType = superTypes.pop();
            if (!context.allTypes.containsKey(superType.getFullName())) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(superType.getFullName());
                if (typeElement == null) {
                    throw new IllegalStateException("Found no type for " + superType.getFullName());
                }
                if (conf.isStrictMode() && !TypeUtils.hasAnnotationOfType(typeElement, conf.getEntityAnnotations())) {
                    continue;
                }
                EntityType superEntityType = elementHandler.handleEntityType(typeElement);
                if (superEntityType.getSuperType() != null) {
                    superTypes.push(superEntityType.getSuperType().getType());
                }
                context.allTypes.put(superType.getFullName(), superEntityType);
            }
        }

    }

    protected Set<TypeElement> collectElements() {
        Set<TypeElement> elements = new HashSet<TypeElement>();

        // from delegate methods
        elements.addAll(processDelegateMethods());

        // from class annotations
        for (Class<? extends Annotation> annotation : conf.getEntityAnnotations()) {
            for (Element element : getElements(annotation)) {
                if (element instanceof TypeElement) {
                    elements.add((TypeElement) element);
                }
            }
        }

        // from package annotations
        if (conf.getEntitiesAnnotation() != null) {
            for (Element element : getElements(conf.getEntitiesAnnotation())) {
                AnnotationMirror mirror = TypeUtils.getAnnotationMirrorOfType(element, conf.getEntitiesAnnotation());
                elements.addAll(TypeUtils.getAnnotationValuesAsElements(mirror, "value"));
            }
        }

        // from embedded annotations
        if (conf.getEmbeddedAnnotation() != null) {
            elements.addAll(getEmbeddedTypes());
        }

        // from embedded
        if (conf.isUnknownAsEmbedded()) {
            elements.addAll(getTypeFromProperties(elements));
        }

        // from annotation less supertypes
        if (!conf.isStrictMode()) {
            elements.addAll(getAnnotationlessSupertypes(elements));
        }

        // register possible embedded types of non-tracked supertypes
        if (conf.getEmbeddedAnnotation() != null) {
            Class<? extends Annotation> embedded = conf.getEmbeddedAnnotation();
            Set<TypeElement> embeddedElements = new HashSet<TypeElement>();
            for (TypeElement element : elements) {
                TypeMirror superTypeMirror = element.getSuperclass();
                while (superTypeMirror != null) {
                    TypeElement superTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(superTypeMirror);
                    if (superTypeElement != null) {
                        List<? extends Element> enclosed = superTypeElement.getEnclosedElements();
                        for (Element child : enclosed) {
                            if (child.getAnnotation(embedded) != null) {
                                handleEmbeddedType(child, embeddedElements);
                            }
                        }
                        superTypeMirror = superTypeElement.getSuperclass();
                        if (superTypeMirror instanceof NoType) {
                            superTypeMirror = null;
                        }
                    } else {
                        superTypeMirror = null;
                    }
                }
            }

            // register found elements
            for (TypeElement element : embeddedElements) {
                if (!elements.contains(element)) {
                    elementHandler.handleEntityType(element);
                }
            }
        }


        return elements;
    }

    private Set<TypeElement> getAnnotationlessSupertypes(Set<TypeElement> elements) {
        Set<TypeElement> rv = new HashSet<TypeElement>();
        for (TypeElement element : elements) {
            TypeMirror superTypeMirror = element.getSuperclass();
            while (superTypeMirror != null) {
                TypeElement superTypeElement = (TypeElement) processingEnv.getTypeUtils().asElement(superTypeMirror);
                if (superTypeElement != null
                        && !superTypeElement.toString().startsWith("java.lang.")
                        && !TypeUtils.hasAnnotationOfType(superTypeElement, conf.getEntityAnnotations())) {
                    rv.add(superTypeElement);
                    superTypeMirror = superTypeElement.getSuperclass();
                    if (superTypeMirror instanceof NoType) {
                        superTypeMirror = null;
                    }
                } else {
                    superTypeMirror = null;
                }
            }
        }
        return rv;
    }

    private void registerTypeElement(String entityName, TypeElement element) {
        Set<TypeElement> elements = context.typeElements.get(entityName);
        if (elements == null) {
            elements = new HashSet<TypeElement>();
            context.typeElements.put(entityName, elements);
        }
        elements.add(element);
    }

    private void processProjectionTypes(Set<TypeElement> elements) {
        Set<Element> visited = new HashSet<Element>();
        for (Element element : getElements(QueryProjection.class)) {
            Element parent = element.getEnclosingElement();
            if (!elements.contains(parent) && !visited.contains(parent)) {
                EntityType model = elementHandler.handleProjectionType((TypeElement) parent);
                registerTypeElement(model.getFullName(), (TypeElement) parent);
                context.projectionTypes.put(model.getFullName(), model);
                visited.add(parent);
            }
        }
    }

    private Set<TypeElement> getEmbeddedTypes() {
        Set<TypeElement> elements = new HashSet<TypeElement>();
        // only creation
        for (Element element : getElements(conf.getEmbeddedAnnotation())) {
            handleEmbeddedType(element, elements);
        }
        return elements;
    }

    private void handleEmbeddedType(Element element, Set<TypeElement> elements) {
        TypeMirror type = element.asType();
        if (element.getKind() == ElementKind.METHOD) {
            type = ((ExecutableElement) element).getReturnType();
        }
        String typeName = type.toString();

        if (typeName.startsWith(Collection.class.getName())
         || typeName.startsWith(List.class.getName())
         || typeName.startsWith(Set.class.getName())) {
            type = ((DeclaredType) type).getTypeArguments().get(0);

        } else if (typeName.startsWith(Map.class.getName())) {
            type = ((DeclaredType) type).getTypeArguments().get(1);
        }

        TypeElement typeElement = typeExtractor.visit(type);

        if (typeElement != null && !TypeUtils.hasAnnotationOfType(typeElement, conf.getEntityAnnotations())) {
            if (!typeElement.getQualifiedName().toString().startsWith("java.")) {
                elements.add(typeElement);
            }
        }
    }

    private Set<TypeElement> getTypeFromProperties(Set<TypeElement> parents) {
        Set<TypeElement> elements = new HashSet<TypeElement>();
        for (Element element : parents) {
            if (element instanceof TypeElement) {
                processFromProperties((TypeElement) element, elements);
            }
        }

        Iterator<TypeElement> iterator = elements.iterator();
        while (iterator.hasNext()) {
            TypeElement element = iterator.next();
            String name = element.getQualifiedName().toString();
            if (name.startsWith("java.") || name.startsWith("org.joda.time.")) {
                iterator.remove();
            } else {
                boolean annotated = false;
                for (Class<? extends Annotation> annotation : conf.getEntityAnnotations()) {
                    annotated |= element.getAnnotation(annotation) != null;
                }
                if (annotated) {
                    iterator.remove();
                }
            }
        }

        return elements;
    }

    private void processFromProperties(TypeElement type, Set<TypeElement> types) {
        List<? extends Element> children = type.getEnclosedElements();
        VisitorConfig config = conf.getConfig(type, children);

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

    private void addSupertypeFields(EntityType model, Set<EntityType> handled) {
        if (handled.add(model)) {
            for (Supertype supertype : model.getSuperTypes()) {
                EntityType entityType = context.allTypes.get(supertype.getType().getFullName());
                if (entityType != null) {
                    addSupertypeFields(entityType, handled);
                    supertype.setEntityType(entityType);
                    model.include(supertype);
                }
            }
        }
    }

    private void processExclusions() {
        for (Element element : getElements(QueryExclude.class)) {
            if (element instanceof PackageElement) {
                conf.addExcludedPackage(((PackageElement) element).getQualifiedName().toString());
            } else if (element instanceof TypeElement) {
                conf.addExcludedClass(((TypeElement) element).getQualifiedName().toString());
            } else {
                throw new IllegalArgumentException(element.toString());
            }
        }
    }

    private Set<TypeElement> processDelegateMethods() {
        Set<? extends Element> delegateMethods = getElements(QueryDelegate.class);
        Set<TypeElement> typeElements = new HashSet<TypeElement>();

        for (Element delegateMethod : delegateMethods) {
            ExecutableElement method = (ExecutableElement) delegateMethod;
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
                    TypeMirror type = TypeUtils.getAnnotationValueAsTypeMirror(annotation, "value");
                    if (type != null) {
                        entityType = typeFactory.getEntityType(type, true);
                    }
                }
            }

            if (entityType != null) {
                registerTypeElement(entityType.getFullName(), (TypeElement) element);
                entityType.addDelegate(new Delegate(entityType, delegateType, name, parameters, returnType));
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(entityType.getFullName());
                boolean isAnnotated = false;
                for (Class<? extends Annotation> ann : conf.getEntityAnnotations()) {
                    if (typeElement.getAnnotation(ann) != null) {
                        isAnnotated = true;
                    }
                }
                if (isAnnotated) {
                    // handle also properties of entity type
                    typeElements.add(processingEnv.getElementUtils().getTypeElement(entityType.getFullName()));
                } else {
                    // skip handling properties
                    context.extensionTypes.put(entityType.getFullName(), entityType);
                    context.allTypes.put(entityType.getFullName(), entityType);
                }
            }
        }

        return typeElements;
    }

    private void validateMetaTypes() {
        @SuppressWarnings("unchecked") // Only concatenated
        Iterable<? extends EntityType> entityTypes = Iterables.concat(
                context.supertypes.values(),
                context.entityTypes.values(),
                context.extensionTypes.values(),
                context.embeddableTypes.values(),
                context.projectionTypes.values());
        for (EntityType entityType : entityTypes) {
            for (Property property : entityType.getProperties()) {
                if (property.getInits() != null && property.getInits().size() > 0) {
                    validateInits(entityType, property);
                }
            }
        }
    }

    protected void validateInits(EntityType entityType, Property property) {
        for (String init : property.getInits()) {
            if (!init.startsWith("*") && property.getType() instanceof EntityType) {
                String initProperty = init.contains(".") ? init.substring(0, init.indexOf('.')) : init;
                if (!((EntityType) property.getType()).getPropertyNames().contains(initProperty)) {
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                        "Illegal inits of " + entityType.getFullName() + "." + property.getName() + ": " +
                        initProperty + " not found");
                }
            }
        }
    }

    private void serializeMetaTypes() {
        if (!context.supertypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Supertypes");
            serialize(conf.getSupertypeSerializer(), context.supertypes.values());
        }

        if (!context.entityTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Entity types");
            serialize(conf.getEntitySerializer(), context.entityTypes.values());
        }

        if (!context.extensionTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Extension types");
            serialize(conf.getEmbeddableSerializer(), context.extensionTypes.values());
        }

        if (!context.embeddableTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Embeddable types");
            serialize(conf.getEmbeddableSerializer(), context.embeddableTypes.values());
        }

        if (!context.projectionTypes.isEmpty()) {
            processingEnv.getMessager().printMessage(Kind.NOTE, "Serializing Projection types");
            serialize(conf.getDTOSerializer(), context.projectionTypes.values());
        }

    }

    private Set<? extends Element> getElements(Class<? extends Annotation> a) {
        return roundEnv.getElementsAnnotatedWith(a);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void serialize(Serializer serializer, Collection<EntityType> models) {
        for (EntityType model : models) {
            try {
                Type type = conf.getTypeMappings().getPathType(model, model, true);
                String packageName = type.getPackageName();
                String className = !packageName.isEmpty() ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();

                // skip if type is excluded class or in excluded package
                if (conf.isExcludedPackage(model.getPackageName()) || conf.isExcludedClass(model.getFullName())) {
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
                JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(className,
                        elements.toArray(new Element[elements.size()]));
                Writer writer = fileObject.openWriter();
                try {
                    SerializerConfig serializerConfig = conf.getSerializerConfig(model);
                    serializer.serialize(model, serializerConfig, new JavaWriter(writer));
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
                processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
            }
        }
    }


    protected abstract Configuration createConfiguration(RoundEnvironment roundEnv);

}
