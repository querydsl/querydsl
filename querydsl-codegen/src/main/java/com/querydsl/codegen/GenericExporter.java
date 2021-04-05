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
package com.querydsl.codegen;

import com.querydsl.codegen.utils.CodeWriter;
import com.querydsl.codegen.utils.JavaWriter;
import com.querydsl.codegen.utils.ScalaWriter;
import com.querydsl.codegen.utils.model.Parameter;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeCategory;
import com.querydsl.codegen.utils.support.ClassUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.annotations.Config;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryExclude;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.core.util.Annotations;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.core.util.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * {@code GenericExporter} provides query type serialization logic for cases where APT annotation processors
 * can't be used. {@code GenericExporter} scans the classpath for classes annotated with specified annotations
 * in specific packages and mirrors them into Querydsl expression types.
 *
 * <p>Example with Querydsl annotations: </p>
 * <pre>
 * {@code
 * GenericExporter exporter = new GenericExporter();
 * exporter.setTargetFolder(new File("target/generated-sources/java"));
 * exporter.export(com.example.domain.Entity.class.getPackage());}
 * </pre>
 *
 * <p>Example with JPA annotations:</p>
 * <pre>
 * {@code
 * GenericExporter exporter = new GenericExporter();
 * exporter.setKeywords(Keywords.JPA);
 * exporter.setEntityAnnotation(Entity.class);
 * exporter.setEmbeddableAnnotation(Embeddable.class);
 * exporter.setEmbeddedAnnotation(Embedded.class);
 * exporter.setSupertypeAnnotation(MappedSuperclass.class);
 * exporter.setSkipAnnotation(Transient.class);
 * exporter.setTargetFolder(new File("target/generated-sources/java"));
 * exporter.export(com.example.domain.Entity.class.getPackage());}
 * </pre>
 *
 * @author tiwe
 *
 */
public class GenericExporter {

    private Class<? extends Annotation> entityAnnotation = QueryEntity.class;

    private Class<? extends Annotation> supertypeAnnotation = QuerySupertype.class;

    private Class<? extends Annotation> embeddableAnnotation = QueryEmbeddable.class;

    private Class<? extends Annotation> embeddedAnnotation = QueryEmbedded.class;

    private Class<? extends Annotation> skipAnnotation = QueryTransient.class;

    private boolean createScalaSources = false;

    private final Set<Class<?>> stopClasses = new HashSet<>();

    private final Map<String, EntityType> allTypes = new HashMap<>();

    private final Map<Class<?>, EntityType> entityTypes = new HashMap<>();

    private final Map<Class<?>, EntityType> superTypes = new HashMap<>();

    private final Map<Class<?>, EntityType> embeddableTypes = new HashMap<>();

    private final Map<Class<?>, EntityType> projectionTypes = new HashMap<>();

    private final CodegenModule codegenModule = new CodegenModule();

    private SerializerConfig serializerConfig = SimpleSerializerConfig.DEFAULT;

    @Deprecated
    private boolean handleFields = true, handleMethods = true;

    private PropertyHandling propertyHandling = PropertyHandling.ALL;

    private boolean useFieldTypes = false;

    @Nullable
    private File targetFolder;

    @Nullable
    private TypeFactory typeFactory;

    private final List<AnnotationHelper> annotationHelpers = new ArrayList<>();

    @Nullable
    private TypeMappings typeMappings;

    @Nullable
    private QueryTypeFactory queryTypeFactory;

    @Nullable
    private Class<? extends Serializer> serializerClass;

    private final Charset charset;

    private final ClassLoader classLoader;

    private Set<File> generatedFiles = new HashSet<File>();

    private boolean strictMode;


    /**
     * Create a GenericExporter instance using the given classloader and charset for serializing
     * source files
     *
     * @param classLoader classloader to use
     * @param charset charset of target sources
     */
    public GenericExporter(ClassLoader classLoader, Charset charset) {
        this.classLoader = classLoader;
        this.charset = charset;
        stopClasses.add(Object.class);
        stopClasses.add(Enum.class);
    }

    /**
     * Create a GenericExporter instance using the given classloader and default charset
     *
     * @param classLoader classloader to use
     */
    public GenericExporter(ClassLoader classLoader) {
        this(classLoader, Charset.defaultCharset());
    }

    /**
     * Create a GenericExporter instance using the context classloader and the given charset
     *
     * @param charset charset of target sources
     */
    public GenericExporter(Charset charset) {
        this(Thread.currentThread().getContextClassLoader(), charset);
    }

    /**
     * Create a GenericExporter instance using the context classloader and default charset
     */
    public GenericExporter() {
        this(Thread.currentThread().getContextClassLoader(), Charset.defaultCharset());
    }

    /**
     * Export the given packages
     *
     * @param packages packages to be scanned
     */
    public void export(Package... packages) {
        String[] pkgs = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            pkgs[i] = packages[i].getName();
        }
        export(pkgs);
    }

    /**
     * Export the given packages
     *
     * @param packages packages to be scanned
     */
    public void export(String... packages) {
        scanPackages(packages);
        innerExport();
    }

    /**
     * Export the given classes
     *
     * @param classes classes to be scanned
     */
    public void export(Class<?>...classes) {
        for (Class<?> cl : classes) {
            handleClass(cl);
        }
        innerExport();
    }

    @SuppressWarnings("unchecked")
    private void innerExport() {
        typeMappings = codegenModule.get(TypeMappings.class);
        queryTypeFactory = codegenModule.get(QueryTypeFactory.class);
        typeFactory = new TypeFactory(Arrays.asList(entityAnnotation, supertypeAnnotation, embeddableAnnotation), codegenModule.get(Function.class, CodegenModule.VARIABLE_NAME_FUNCTION_CLASS));

        // copy annotations helpers to typeFactory
        for (AnnotationHelper helper : annotationHelpers) {
            typeFactory.addAnnotationHelper(helper);
        }

        // process supertypes
        for (Class<?> cl : superTypes.keySet()) {
            createEntityType(cl, superTypes);
        }

        // process embeddables
        for (Class<?> cl : embeddableTypes.keySet()) {
            createEntityType(cl, embeddableTypes);
        }

        // process entities
        for (Class<?> cl : entityTypes.keySet()) {
            createEntityType(cl, entityTypes);
        }

        // process projections
        for (Class<?> cl : projectionTypes.keySet()) {
            createEntityType(cl, projectionTypes);
        }

        // add constructors and properties
        for (Map<Class<?>, EntityType> entries : Arrays.asList(superTypes, embeddableTypes, entityTypes, projectionTypes)) {
            for (Map.Entry<Class<?>, EntityType> entry : new HashSet<>(entries.entrySet())) {
                addConstructors(entry.getKey(), entry.getValue());
                addProperties(entry.getKey(), entry.getValue());
            }
        }

        // merge supertype fields into subtypes
        Set<EntityType> handled = new HashSet<EntityType>();
        for (EntityType type : superTypes.values()) {
            addSupertypeFields(type, allTypes, handled);
        }
        for (EntityType type : entityTypes.values()) {
            addSupertypeFields(type, allTypes, handled);
        }
        for (EntityType type : embeddableTypes.values()) {
            addSupertypeFields(type, allTypes, handled);
        }

        // extend types
        typeFactory.extendTypes();

        try {
            Serializer supertypeSerializer, entitySerializer, embeddableSerializer, projectionSerializer;

            if (serializerClass != null) {
                Serializer serializer = codegenModule.get(serializerClass);
                supertypeSerializer = serializer;
                entitySerializer = serializer;
                embeddableSerializer = serializer;
                projectionSerializer = serializer;
            } else {
                supertypeSerializer = codegenModule.get(DefaultSupertypeSerializer.class);
                entitySerializer = codegenModule.get(DefaultEntitySerializer.class);
                embeddableSerializer = codegenModule.get(DefaultEmbeddableSerializer.class);
                projectionSerializer = codegenModule.get(DefaultProjectionSerializer.class);
            }

            // serialize super types
            serialize(supertypeSerializer, superTypes);

            // serialize entity types
            serialize(entitySerializer, entityTypes);

            // serialize embeddable types
            serialize(embeddableSerializer, embeddableTypes);

            // serialize projection types
            serialize(projectionSerializer, projectionTypes);

        } catch (IOException e) {
            throw new QueryException(e);
        }

    }

    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes,
            Set<EntityType> handled) {
        if (handled.add(model)) {
            for (Supertype supertype : model.getSuperTypes()) {
                EntityType entityType = superTypes.get(supertype.getType().getFullName());
                if (entityType == null) {
                    if (supertype.getType().getPackageName().startsWith("java.")) {
                        // skip internal supertypes
                        continue;
                    }
                    // FIXME this misses the generics
                    Class<?> cl = supertype.getType().getJavaClass();
                    typeFactory.addEmbeddableType(cl);
                    entityType = createEntityType(cl, new HashMap<Class<?>, EntityType>());
                    addProperties(cl, entityType);

                }
                addSupertypeFields(entityType, superTypes, handled);
                supertype.setEntityType(entityType);
                model.include(supertype);
            }
        }
    }

    private boolean containsAny(Class<?> clazz, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annType : annotations) {
            if (clazz.isAnnotationPresent(annType)) {
                return true;
            }
        }
        return false;
    }

    private EntityType createEntityType(Class<?> cl, Map<Class<?>, EntityType> types) {
        if (types.get(cl) != null) {
            return types.get(cl);
        } else {
            EntityType type = allTypes.get(ClassUtils.getFullName(cl));
            if (type == null) {
                type = typeFactory.getEntityType(cl);
            }
            types.put(cl, type);
            String fullName = ClassUtils.getFullName(cl);
            if (!allTypes.containsKey(fullName)) {
                allTypes.put(fullName, type);
            }

            typeMappings.register(type, queryTypeFactory.create(type));

            if (strictMode && cl.getSuperclass() != null) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation>[] annotations =
                        (Class<? extends Annotation>[]) new Class<?>[] {entityAnnotation, supertypeAnnotation, embeddableAnnotation};
                if (!containsAny(cl.getSuperclass(), annotations)) {
                    // skip supertype handling
                    return type;
                }
            }

            if (cl.getSuperclass() != null && !stopClasses.contains(cl.getSuperclass())
                && !cl.getSuperclass().isAnnotationPresent(QueryExclude.class)) {
                type.addSupertype(new Supertype(typeFactory.get(cl.getSuperclass(), cl.getGenericSuperclass())));
            }
            if (cl.isInterface()) {
                for (Class<?> iface : cl.getInterfaces()) {
                    if (!stopClasses.contains(iface) && !iface.isAnnotationPresent(QueryExclude.class)) {
                        type.addSupertype(new Supertype(typeFactory.get(iface)));
                    }
                }
            }

            return type;
        }
    }

    private void addConstructors(Class<?> cl, EntityType type) {
        for (Constructor<?> constructor : cl.getConstructors()) {
            if (constructor.isAnnotationPresent(QueryProjection.class)) {
                List<Parameter> parameters = new ArrayList<>();
                for (int i = 0; i < constructor.getParameterTypes().length; i++) {
                    Type parameterType = typeFactory.get(
                            constructor.getParameterTypes()[i],
                            constructor.getGenericParameterTypes()[i]);
                    for (Annotation annotation : constructor.getParameterAnnotations()[i]) {
                        if (annotation.annotationType().equals(QueryType.class)) {
                            QueryType queryType = (QueryType) annotation;
                            parameterType = parameterType.as(TypeCategory.valueOf(queryType.value().name()));
                        }
                    }
                    parameters.add(new Parameter("param" + i, parameterType));
                }
                type.addConstructor(new com.querydsl.codegen.utils.model.Constructor(parameters));
            }
        }
    }

    private void addProperties(Class<?> cl, EntityType type) {
        Map<String, Type> types = new HashMap<>();
        Map<String, Annotations> annotations = new HashMap<>();

        PropertyHandling.Config config = propertyHandling.getConfig(cl);

        // fields
        if (config.isFields()) {
            for (Field field : cl.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (Modifier.isTransient(field.getModifiers()) && !field.isAnnotationPresent(QueryType.class)) {
                        continue;
                    }
                    AnnotatedElement annotated = ReflectionUtils.getAnnotatedElement(cl, field.getName(), field.getType());
                    Type propertyType = getPropertyType(cl, annotated, field.getType(), field.getGenericType());
                    Annotations ann = new Annotations(field);
                    types.put(field.getName(), propertyType);
                    annotations.put(field.getName(), ann);
                }
            }
        }

        // getters
        if (config.isMethods()) {
            for (Method method : cl.getDeclaredMethods()) {
                String name = method.getName();
                if (method.getParameterTypes().length == 0
                    && !Modifier.isStatic(method.getModifiers())
                    && !method.isBridge()
                    && ((name.startsWith("get") && name.length() > 3)
                     || (name.startsWith("is") && name.length() > 2))) {
                    String propertyName;
                    if (name.startsWith("get")) {
                        propertyName = BeanUtils.uncapitalize(name.substring(3));
                    } else {
                        propertyName = BeanUtils.uncapitalize(name.substring(2));
                    }
                    Type propertyType = getPropertyType(cl, method, method.getReturnType(), method.getGenericReturnType());
                    if (!types.containsKey(propertyName) || !useFieldTypes) {
                        types.put(propertyName, propertyType);
                    }
                    Annotations ann = annotations.get(propertyName);
                    if (ann == null) {
                        ann = new Annotations();
                        annotations.put(propertyName, ann);
                    }
                    ann.addAnnotations(method);
                }
            }
        }

        for (Map.Entry<String, Type> entry : types.entrySet()) {
            Annotations ann = annotations.get(entry.getKey());
            Property property = createProperty(type, entry.getKey(), entry.getValue(), ann);
            if (property != null) {
                type.addProperty(property);
            }
        }
    }

    private Type getPropertyType(Class<?> cl, AnnotatedElement annotated, Class<?> type,
            java.lang.reflect.Type genericType) {
        Type propertyType = null;
        if (annotated.isAnnotationPresent(embeddedAnnotation)) {
            Class<?> embeddableType = type;
            if (Collection.class.isAssignableFrom(type)) {
                embeddableType = ReflectionUtils.getTypeParameterAsClass(genericType, 0);
            } else if (Map.class.isAssignableFrom(type)) {
                embeddableType = ReflectionUtils.getTypeParameterAsClass(genericType, 1);
            }
            if (!embeddableType.getName().startsWith("java.")) {
                typeFactory.addEmbeddableType(embeddableType);
                if (!embeddableTypes.containsKey(embeddableType)
                  && !entityTypes.containsKey(embeddableType)
                  && !superTypes.containsKey(embeddableType)) {
                    EntityType entityType = createEntityType(embeddableType, embeddableTypes);
                    addProperties(embeddableType, entityType);
                    if (embeddableType == type) {
                        propertyType = entityType;
                    }
                }
            }
        }
        if (propertyType == null) {
            propertyType = typeFactory.get(type, annotated, genericType);
            if (propertyType instanceof EntityType && !allTypes.containsKey(ClassUtils.getFullName(type))) {
                String fullName = ClassUtils.getFullName(type);
                if (!allTypes.containsKey(fullName)) {
                    allTypes.put(fullName, (EntityType) propertyType);
                }
            }
        }
        return propertyType;
    }

    @Nullable
    private Property createProperty(EntityType entityType, String propertyName, Type propertyType,
            AnnotatedElement annotated) {
        List<String> inits = Collections.emptyList();
        if (annotated.isAnnotationPresent(skipAnnotation)
            && !annotated.isAnnotationPresent(QueryType.class)) {
            return null;
        }
        if (annotated.isAnnotationPresent(QueryInit.class)) {
            inits = Arrays.asList(annotated.getAnnotation(QueryInit.class).value());
        }
        if (annotated.isAnnotationPresent(QueryType.class)) {
            QueryType queryType = annotated.getAnnotation(QueryType.class);
            if (queryType.value().equals(PropertyType.NONE)) {
                return null;
            }
            propertyType = propertyType.as(TypeCategory.valueOf(queryType.value().name()));
        }
        return new Property(entityType, propertyName, propertyType, inits);
    }

    private void scanPackages(String... packages) {
        if (packages == null) {
            return;
        }
        for (String pkg : packages) {
            try {
                for (Class<?> cl : ClassPathUtils.scanPackage(classLoader, pkg)) {
                    handleClass(cl);
                }
            } catch (IOException e) {
                throw new QueryException(e);
            }
        }
    }

    private void handleClass(Class<?> cl) {
        if (stopClasses.contains(cl) || cl.isAnnotationPresent(QueryExclude.class)) {
            return;
        } else if (cl.isAnnotationPresent(entityAnnotation)) {
            entityTypes.put(cl, null);
        } else if (cl.isAnnotationPresent(embeddableAnnotation)) {
            embeddableTypes.put(cl, null);
        } else if (cl.isAnnotationPresent(supertypeAnnotation)) {
            superTypes.put(cl, null);
        } else {
            for (Constructor<?> constructor : cl.getConstructors()) {
                if (constructor.isAnnotationPresent(QueryProjection.class)) {
                    projectionTypes.put(cl, null);
                    break;
                }
            }
        }
    }

    private void serialize(Serializer serializer, Map<Class<?>, EntityType> types) throws IOException {
        for (Map.Entry<Class<?>, EntityType> entityType : types.entrySet()) {
            Type type = typeMappings.getPathType(entityType.getValue(), entityType.getValue(), true);
            String packageName = type.getPackageName();
            String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
            SerializerConfig config = serializerConfig;
            if (entityType.getKey().isAnnotationPresent(Config.class)) {
                config = SimpleSerializerConfig.getConfig(entityType.getKey().getAnnotation(Config.class));
            }
            String fileSuffix = createScalaSources ? ".scala" : ".java";
            write(serializer, className.replace('.', '/') + fileSuffix, config, entityType.getValue());
        }
    }

    private void write(Serializer serializer, String path, SerializerConfig serializerConfig,
            EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
        generatedFiles.add(targetFile);
        try (Writer w = writerFor(targetFile)) {
            CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
            serializer.serialize(type, serializerConfig, writer);
        }
    }

    private Writer writerFor(File file) {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            System.err.println("Folder " + file.getParent() + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file), charset);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * Return the set of generated files
     *
     * @return a set of generated files
     */
    public Set<File> getGeneratedFiles() {
        return generatedFiles;
    }

    /**
     * Set the entity annotation
     *
     * @param entityAnnotation entity annotation
     */
    public void setEntityAnnotation(Class<? extends Annotation> entityAnnotation) {
        this.entityAnnotation = entityAnnotation;
    }

    /**
     * Set the supertype annotation
     *
     * @param supertypeAnnotation supertype annotation
     */
    public void setSupertypeAnnotation(
            Class<? extends Annotation> supertypeAnnotation) {
        this.supertypeAnnotation = supertypeAnnotation;
    }

    /**
     * Set the embeddable annotation
     *
     * @param embeddableAnnotation embeddable annotation
     */
    public void setEmbeddableAnnotation(
            Class<? extends Annotation> embeddableAnnotation) {
        this.embeddableAnnotation = embeddableAnnotation;
    }

    /**
     * Set the embedded annotation
     *
     * @param embeddedAnnotation embedded annotation
     */
    public void setEmbeddedAnnotation(Class<? extends Annotation> embeddedAnnotation) {
        this.embeddedAnnotation = embeddedAnnotation;
    }

    /**
     * Set the skip annotation
     *
     * @param skipAnnotation skip annotation
     */
    public void setSkipAnnotation(Class<? extends Annotation> skipAnnotation) {
        this.skipAnnotation = skipAnnotation;
    }

    /**
     * Set the target folder for generated sources
     *
     * @param targetFolder
     */
    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    /**
     * Set the serializer class to be used
     *
     * @param serializerClass
     */
    public void setSerializerClass(Class<? extends Serializer> serializerClass) {
        codegenModule.bind(serializerClass);
        this.serializerClass = serializerClass;
    }

    /**
     * Set the typemappings class to be used
     *
     * @param typeMappingsClass
     */
    public void setTypeMappingsClass(Class<? extends TypeMappings> typeMappingsClass) {
        codegenModule.bind(TypeMappings.class, typeMappingsClass);
    }

    /**
     * Set whether Scala sources are generated
     *
     * @param createScalaSources
     */
    public void setCreateScalaSources(boolean createScalaSources) {
        this.createScalaSources = createScalaSources;
    }

    /**
     * Set the keywords to be used
     *
     * @param keywords
     */
    public void setKeywords(Collection<String> keywords) {
        codegenModule.bind(CodegenModule.KEYWORDS, keywords);
    }

    /**
     * Set the name prefix
     *
     * @param prefix
     */
    public void setNamePrefix(String prefix) {
        codegenModule.bind(CodegenModule.PREFIX, prefix);
    }

    /**
     * Set the name suffix
     *
     * @param suffix
     */
    public void setNameSuffix(String suffix) {
        codegenModule.bind(CodegenModule.SUFFIX, suffix);
    }

    /**
     * Set the package suffix
     *
     * @param suffix
     */
    public void setPackageSuffix(String suffix) {
        codegenModule.bind(CodegenModule.PACKAGE_SUFFIX, suffix);
    }

    /**
     * Set whether fields are handled (default true)
     *
     * @param b
     * @deprecated Use {@link #setPropertyHandling(PropertyHandling)} instead
     */
    @Deprecated
    public void setHandleFields(boolean b) {
        handleFields = b;
        setPropertyHandling();
    }

    /**
     * Set whether methods are handled (default true)
     *
     * @param b
     * @deprecated Use {@link #setPropertyHandling(PropertyHandling)} instead
     */
    @Deprecated
    public void setHandleMethods(boolean b) {
        handleMethods = b;
        setPropertyHandling();
    }

    private void setPropertyHandling() {
        if (handleFields) {
            propertyHandling = handleMethods ? PropertyHandling.ALL : PropertyHandling.FIELDS;
        } else if (handleMethods) {
            propertyHandling = PropertyHandling.METHODS;
        } else {
            propertyHandling = PropertyHandling.NONE;
        }
    }

    /**
     * Set the property handling mode
     *
     * @param propertyHandling
     */
    public void setPropertyHandling(PropertyHandling propertyHandling) {
        this.propertyHandling = propertyHandling;
    }

    /**
     * Set whether field types should be used instead of getter return types (default false)
     *
     * @param b
     */
    public void setUseFieldTypes(boolean b) {
        useFieldTypes = b;
    }

    /**
     * Add a stop class to be used (default Object.class and Enum.class)
     *
     * @param cl
     */
    public void addStopClass(Class<?> cl) {
        stopClasses.add(cl);
    }

    /**
     * Set whether annotationless superclasses are handled or not (default: true)
     *
     * @param s
     */
    public void setStrictMode(boolean s) {
        strictMode = s;
    }

    /**
     * Set the serializer configuration to use
     *
     * @param serializerConfig
     */
    public void setSerializerConfig(SerializerConfig serializerConfig) {
        this.serializerConfig = serializerConfig;
    }

    /**
     * Add a annotation helper object to process custom annotations
     *
     * @param annotationHelper
     */
    public void addAnnotationHelper(AnnotationHelper annotationHelper) {
        annotationHelpers.add(annotationHelper);
    }

    /**
     * Set the Generated annotation class. Will default to java {@code @Generated}
     *
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element) to be used on
     *                                 the generated sources, or {@code null} (defaulting to {@code javax.annotation.Generated} or
     *                                {@code javax.annotation.processing.Generated} depending on the java version).
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    public void setGeneratedAnnotationClass(@Nullable String generatedAnnotationClass) {
        codegenModule.bindInstance(CodegenModule.GENERATED_ANNOTATION_CLASS, GeneratedAnnotationResolver.resolve(generatedAnnotationClass));
    }
}
