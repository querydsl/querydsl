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
package com.mysema.query.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.JavaWriter;
import com.mysema.codegen.ScalaWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.support.ClassUtils;
import com.mysema.query.QueryException;
import com.mysema.query.annotations.Config;
import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;
import com.mysema.util.BeanUtils;
import com.mysema.util.ClassPathUtils;
import com.mysema.util.ReflectionUtils;

/**
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
    
    private final Map<String, EntityType> allTypes = new HashMap<String, EntityType>();

    private final Map<Class<?>, EntityType> entityTypes = new HashMap<Class<?>, EntityType>();

    private final Map<Class<?>, EntityType> superTypes = new HashMap<Class<?>, EntityType>();

    private final Map<Class<?>, EntityType> embeddableTypes = new HashMap<Class<?>, EntityType>();

    private final CodegenModule codegenModule = new CodegenModule();

    private final SerializerConfig serializerConfig = SimpleSerializerConfig.DEFAULT;
    
    private boolean handleFields = true, handleMethods = true;

    @Nullable
    private File targetFolder;

    @Nullable
    private TypeFactory typeFactory;

    @Nullable
    private TypeMappings typeMappings;

    @Nullable
    private QueryTypeFactory queryTypeFactory;

    @Nullable
    private Class<? extends Serializer> serializerClass;
    
    public void export(Package... packages) {
        String[] pkgs = new String[packages.length];
        for (int i = 0; i < packages.length; i++) {
            pkgs[i] = packages[i].getName();
        }
        export(pkgs);
    }

    @SuppressWarnings("unchecked")
    public void export(String... packages){
        scanPackages(packages);

        typeMappings = codegenModule.get(TypeMappings.class);
        queryTypeFactory = codegenModule.get(QueryTypeFactory.class);
        typeFactory = new TypeFactory(entityAnnotation, supertypeAnnotation, embeddableAnnotation);

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

        // add properties
        for (Map<Class<?>, EntityType> entries : Arrays.asList(superTypes, embeddableTypes, entityTypes)) {
            for (Map.Entry<Class<?>, EntityType> entry : entries.entrySet()) {
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

        try {
            Serializer supertypeSerializer, entitySerializer, embeddableSerializer;

            if (serializerClass != null) {
                Serializer serializer = codegenModule.get(serializerClass);
                supertypeSerializer = serializer;
                entitySerializer = serializer;
                embeddableSerializer = serializer;
            } else {
                supertypeSerializer = codegenModule.get(SupertypeSerializer.class);
                entitySerializer = codegenModule.get(EntitySerializer.class);
                embeddableSerializer = codegenModule.get(EmbeddableSerializer.class);
            }

            // serialize super types
            serialize(supertypeSerializer, superTypes);

            // serialze entity types
            serialize(entitySerializer, entityTypes);

            // serialize embeddables
            serialize(embeddableSerializer, embeddableTypes);

        } catch (IOException e) {
            throw new QueryException(e);
        }

    }

    private void addSupertypeFields(EntityType model, Map<String, EntityType> superTypes, Set<EntityType> handled) {
        if (handled.add(model)) {
            for (Supertype supertype : model.getSuperTypes()) {
                EntityType entityType = superTypes.get(supertype.getType().getFullName());
                if (entityType == null) {
                    if (supertype.getType().getPackageName().startsWith("java.")){
                        // skip internal supertypes
                        continue;
                    }
                    try {
                        Class<?> cl = Class.forName(supertype.getType().getFullName());
                        typeFactory.addEmbeddableType(cl);
                        entityType = createEntityType(cl, new HashMap<Class<?>, EntityType>());
                        addProperties(cl, entityType);
                    } catch (ClassNotFoundException e) {
                        throw new QueryException(e);
                    }
                }
                addSupertypeFields(entityType, superTypes, handled);
                supertype.setEntityType(entityType);
                model.include(supertype);
            }
        }
    }

    private EntityType createEntityType(Class<?> cl, Map<Class<?>, EntityType> types) {
//        System.err.println(cl.getName());
        if (types.get(cl) != null) {
            return types.get(cl);
        } else {
            EntityType type = allTypes.get(ClassUtils.getFullName(cl));
            if (type == null) {
                type = (EntityType)typeFactory.create(cl);
            }
            types.put(cl, type);
            allTypes.put(ClassUtils.getFullName(cl), type);

            typeMappings.register(type, queryTypeFactory.create(type));
            if (cl.getSuperclass() != null && !cl.getSuperclass().equals(Object.class)) {
                type.addSupertype(new Supertype(new ClassType(cl.getSuperclass())));
            }
            if (cl.isInterface()) {
                for (Class<?> iface : cl.getInterfaces()){
                    type.addSupertype(new Supertype(new ClassType(iface)));
                }
            }

            return type;
        }
    }

    private void addProperties(Class<?> cl, EntityType type) {
        Set<String> handled = new HashSet<String>();
        
        // fields
        if (handleFields) {
            for (Field field : cl.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
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
                if (method.getParameterTypes().length == 0
                    && (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
                    String propertyName;
                    if (method.getName().startsWith("get")) {
                        propertyName = BeanUtils.uncapitalize(method.getName().substring(3));
                    } else {
                        propertyName = BeanUtils.uncapitalize(method.getName().substring(2));
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

    private Type getPropertyType(Class<?> cl, AnnotatedElement annotated, Class<?> type, java.lang.reflect.Type genericType) {
        Type propertyType = allTypes.get(ClassUtils.getFullName(type));
        if (propertyType == null && annotated.isAnnotationPresent(embeddedAnnotation)) {
            Class<?> embeddableType = type;
            if (Collection.class.isAssignableFrom(type)) {
                embeddableType = ReflectionUtils.getTypeParameter(genericType, 0);
            } else if (Map.class.isAssignableFrom(type)) {
                embeddableType = ReflectionUtils.getTypeParameter(genericType, 1);
            }
            typeFactory.addEmbeddableType(embeddableType);
            if (!embeddableTypes.containsKey(embeddableType) && !entityTypes.containsKey(embeddableType)) {
                EntityType entityType = createEntityType(embeddableType, embeddableTypes);
                addProperties(embeddableType, entityType);
            }
        }
        if (propertyType == null) {
            propertyType = typeFactory.create(type, genericType);
            if (propertyType instanceof EntityType && !allTypes.containsKey(ClassUtils.getFullName(type))) {
                allTypes.put(ClassUtils.getFullName(type), (EntityType)propertyType);
            }
        }
        return propertyType;
    }

    @Nullable
    private Property createProperty(EntityType entityType, String propertyName, Type propertyType, AnnotatedElement annotated) {
        String[] inits = new String[0];
        if (annotated.isAnnotationPresent(skipAnnotation)) {
            return null;
        }
        if (annotated.isAnnotationPresent(QueryInit.class)) {
            inits = annotated.getAnnotation(QueryInit.class).value();
        }
        if (annotated.isAnnotationPresent(QueryType.class)) {
            QueryType queryType = annotated.getAnnotation(QueryType.class);
            if (queryType.value().equals(PropertyType.NONE)) {
                return null;
            }
            propertyType = propertyType.as(queryType.value().getCategory());
        }
        return new Property(entityType, propertyName, propertyType, inits);
    }


    private void scanPackages(String... packages){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String pkg : packages) {
            try {
                for (Class<?> cl : ClassPathUtils.scanPackage(classLoader, pkg)) {
                    if (cl.getAnnotation(embeddableAnnotation) != null){
                        embeddableTypes.put(cl, null);
                    } else if (cl.getAnnotation(supertypeAnnotation) != null) {
                        superTypes.put(cl, null);
                    } else if (cl.getAnnotation(entityAnnotation) != null) {
                        entityTypes.put(cl, null);
                    }
                }
            } catch (IOException e) {
                throw new QueryException(e);
            }
        }
    }

    private void serialize(Serializer serializer, Map<Class<?>, EntityType> types) throws IOException {
        for (Map.Entry<Class<?>, EntityType> entityType : types.entrySet()) {
            Type type = typeMappings.getPathType(entityType.getValue(), entityType.getValue(), true);
            String packageName = type.getPackageName();
            String className = packageName.length() > 0 ? (packageName + "." + type.getSimpleName()) : type.getSimpleName();
            SerializerConfig config = serializerConfig;
            if (entityType.getKey().isAnnotationPresent(Config.class)){
                config = SimpleSerializerConfig.getConfig(entityType.getKey().getAnnotation(Config.class));
            }
            String fileSuffix = createScalaSources ? ".scala" : ".java";
            write(serializer, className.replace('.', '/') + fileSuffix, config, entityType.getValue());
        }
    }

    private void write(Serializer serializer, String path, SerializerConfig serializerConfig, EntityType type) throws IOException {
        File targetFile = new File(targetFolder, path);
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
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void setEntityAnnotation(Class<? extends Annotation> entityAnnotation) {
        this.entityAnnotation = entityAnnotation;
    }

    public void setSupertypeAnnotation(
            Class<? extends Annotation> supertypeAnnotation) {
        this.supertypeAnnotation = supertypeAnnotation;
    }

    public void setEmbeddableAnnotation(
            Class<? extends Annotation> embeddableAnnotation) {
        this.embeddableAnnotation = embeddableAnnotation;
    }

    public void setEmbeddedAnnotation(Class<? extends Annotation> embeddedAnnotation) {
        this.embeddedAnnotation = embeddedAnnotation;
    }

    public void setSkipAnnotation(Class<? extends Annotation> skipAnnotation) {
        this.skipAnnotation = skipAnnotation;
    }

    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setSerializerClass(Class<? extends Serializer> serializerClass) {
        codegenModule.bind(serializerClass);
        this.serializerClass = serializerClass;
    }

    public void setCreateScalaSources(boolean createScalaSources) {
        this.createScalaSources = createScalaSources;
    }
    
    public void setNamePrefix(String prefix) {
        codegenModule.bind(CodegenModule.PREFIX, prefix);
    }
    
    public void setNameSuffix(String suffix) {
        codegenModule.bind(CodegenModule.SUFFIX, suffix);
    }

    public void setPackageSuffix(String suffix) {
        codegenModule.bind(CodegenModule.PACKAGE_SUFFIX, suffix);
    }
    
    public void setHandleFields(boolean b) {
        handleFields = b;
    }
    
    public void setHandleMethods(boolean b) {
        handleMethods = b;
    }
    
}
