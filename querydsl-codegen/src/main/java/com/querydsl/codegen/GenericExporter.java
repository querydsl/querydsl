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
package com.querydsl.codegen;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.support.ClassUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.annotations.*;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.core.util.ClassPathUtils;
import com.querydsl.core.util.ReflectionUtils;

/**
 * GenericExporter provides querydsl type serialization logic for cases where APT annotation processors
 * can't be used. GenericExporter scans the classpath for classes annotated with specified annotations
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

    private final Set<Class<?>> stopClasses = Sets.newHashSet();

    private final Map<String, EntityType> allTypes = Maps.newHashMap();

    private final Map<Class<?>, EntityType> entityTypes = Maps.newHashMap();

    private final Map<Class<?>, EntityType> superTypes = Maps.newHashMap();

    private final Map<Class<?>, EntityType> embeddableTypes = Maps.newHashMap();

    private final Map<Class<?>, EntityType> projectionTypes = Maps.newHashMap();

    private final CodegenModule codegenModule = new CodegenModule();

    private final SerializerConfig serializerConfig = SimpleSerializerConfig.DEFAULT;

    private boolean handleFields = true, handleMethods = true;

    @Nullable
    private File targetFolder;
    
    @Nullable
    private TypeFactory typeFactory;
    
    private final List<AnnotationHelper> annotationHelpers = Lists.newArrayList();

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
     * @param classLoader
     * @param charset
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
     * @param classLoader
     */
    public GenericExporter(ClassLoader classLoader) {
        this(classLoader, Charset.defaultCharset());
    }

    /**
     * Create a GenericExporter instance using the context classloader and the given charset
     *
     * @param charset
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
     * @param packages
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
     * @param packages
     */
    public void export(String... packages) {
        scanPackages(packages);
        innerExport();
    }

    /**
     * Export the given classes
     *
     * @param classes
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
        typeFactory = new TypeFactory(ImmutableList.of(entityAnnotation, supertypeAnnotation, embeddableAnnotation));

        // copy annotations helpers to typeFactory
        for (AnnotationHelper helper : annotationHelpers){
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
            for (Map.Entry<Class<?>, EntityType> entry : Sets.newHashSet(entries.entrySet())) {
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
                supertypeSerializer = codegenModule.get(SupertypeSerializer.class);
                entitySerializer = codegenModule.get(EntitySerializer.class);
                embeddableSerializer = codegenModule.get(EmbeddableSerializer.class);
                projectionSerializer = codegenModule.get(ProjectionSerializer.class);
            }

            // serialize super types
            serialize(supertypeSerializer, superTypes);

            // serialze entity types
            serialize(entitySerializer, entityTypes);

            // serialize embeddables
            serialize(embeddableSerializer, embeddableTypes);

            // serialize projections
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

            if (strictMode && cl.getSuperclass() != null && !containsAny(cl.getSuperclass(),
                    entityAnnotation, supertypeAnnotation, embeddableAnnotation)) {
                // skip supertype handling
                return type;
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
                List<Parameter> parameters = Lists.newArrayList();
                for (int i = 0; i < constructor.getParameterTypes().length; i++) {
                    Type parameterType = typeFactory.get(
                            constructor.getParameterTypes()[i],
                            constructor.getGenericParameterTypes()[i]);
                    for (Annotation annotation : constructor.getParameterAnnotations()[i]) {
                        if (annotation.annotationType().equals(QueryType.class)) {
                            QueryType queryType = (QueryType)annotation;
                            parameterType = parameterType.as(TypeCategory.valueOf(queryType.value().name()));
                        }
                    }
                    parameters.add(new Parameter("param" + i, parameterType));
                }
                type.addConstructor(new com.mysema.codegen.model.Constructor(parameters));
            }
        }
    }

    private void addProperties(Class<?> cl, EntityType type) {
        Set<String> handled = new HashSet<String>();

        // fields
        if (handleFields) {
            for (Field field : cl.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    if (Modifier.isTransient(field.getModifiers()) && !field.isAnnotationPresent(QueryType.class)) {
                        continue;
                    }
                    AnnotatedElement annotated = ReflectionUtils.getAnnotatedElement(cl, field.getName(), field.getType());
                    Method method = ReflectionUtils.getGetterOrNull(cl, field.getName(), field.getType());
                    Type propertyType = null;
                    if (method != null) {
                        propertyType = getPropertyType(cl, annotated, method.getReturnType(), method.getGenericReturnType());
                    } else {
                        propertyType = getPropertyType(cl, annotated, field.getType(), field.getGenericType());
                    }
                    Property property = createProperty(type, field.getName(), propertyType, field);
                    if (property != null) {
                        type.addProperty(property);
                    }
                    handled.add(field.getName());
                }
            }
        }

        // getters
        if (handleMethods) {
            for (Method method : cl.getDeclaredMethods()) {
                String name = method.getName();
                if (method.getParameterTypes().length == 0
                    && ((name.startsWith("get") && name.length() > 3)
                     || (name.startsWith("is") && name.length() > 2))) {
                    String propertyName;
                    if (name.startsWith("get")) {
                        propertyName = BeanUtils.uncapitalize(name.substring(3));
                    } else {
                        propertyName = BeanUtils.uncapitalize(name.substring(2));
                    }
                    if (handled.contains(propertyName)) {
                        continue;
                    }
                    Type propertyType = getPropertyType(cl, method, method.getReturnType(), method.getGenericReturnType());
                    Property property = createProperty(type, propertyName, propertyType, method);
                    if (property != null) {
                        type.addProperty(property);
                    }
                }
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
                    allTypes.put(fullName, (EntityType)propertyType);
                }
            }
        }
        return propertyType;
    }

    @Nullable
    private Property createProperty(EntityType entityType, String propertyName, Type propertyType,
            AnnotatedElement annotated) {
        List<String> inits = Collections.<String>emptyList();
        if (annotated.isAnnotationPresent(skipAnnotation)
            && !annotated.isAnnotationPresent(QueryType.class)) {
            return null;
        }
        if (annotated.isAnnotationPresent(QueryInit.class)) {
            inits = ImmutableList.copyOf(annotated.getAnnotation(QueryInit.class).value());
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
        Writer w = writerFor(targetFile);
        try {
            CodeWriter writer = createScalaSources ? new ScalaWriter(w) : new JavaWriter(w);
            serializer.serialize(type, serializerConfig, writer);
        } finally {
            w.close();
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
     * @return
     */
    public Set<File> getGeneratedFiles() {
        return generatedFiles;
    }

    /**
     * Set the entity annotation
     *
     * @param entityAnnotation
     */
    public void setEntityAnnotation(Class<? extends Annotation> entityAnnotation) {
        this.entityAnnotation = entityAnnotation;
    }

    /**
     * Set the supertype annotation
     *
     * @param supertypeAnnotation
     */
    public void setSupertypeAnnotation(
            Class<? extends Annotation> supertypeAnnotation) {
        this.supertypeAnnotation = supertypeAnnotation;
    }

    /**
     * Set the embeddable annotation
     *
     * @param embeddableAnnotation
     */
    public void setEmbeddableAnnotation(
            Class<? extends Annotation> embeddableAnnotation) {
        this.embeddableAnnotation = embeddableAnnotation;
    }

    /**
     * Set the embedded annotation
     *
     * @param embeddedAnnotation
     */
    public void setEmbeddedAnnotation(Class<? extends Annotation> embeddedAnnotation) {
        this.embeddedAnnotation = embeddedAnnotation;
    }

    /**
     * Set the skip annotation
     *
     * @param skipAnnotation
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
     */
    public void setHandleFields(boolean b) {
        handleFields = b;
    }

    /**
     * Set whether fields are handled (default true)
     *
     * @param b
     */
    public void setHandleMethods(boolean b) {
        handleMethods = b;
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
     * Add a annotation helper object to process custom annotations
     * 
     * @param annotationHelper 
     */
    public void addAnnotationHelper(AnnotationHelper annotationHelper){
        annotationHelpers.add(annotationHelper);
    }
}
