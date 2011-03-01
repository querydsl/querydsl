/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.Config;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.codegen.EmbeddableSerializer;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.ProjectionSerializer;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SupertypeSerializer;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.types.Expression;

/**
 * DefaultConfiguration is a simple implementation of the Configuration interface
 *
 * @author tiwe
 *
 */
public class DefaultConfiguration implements Configuration {

    private static final String QUERYDSL_CREATE_DEFAULT_VARIABLE = "querydsl.createDefaultVariable";

    private static final String QUERYDSL_MAP_ACCESSORS = "querydsl.mapAccessors";

    private static final String QUERYDSL_LIST_ACCESSORS = "querydsl.listAccessors";

    private static final String QUERYDSL_ENTITY_ACCESSORS = "querydsl.entityAccessors";

    private static final String DEFAULT_OVERWRITE = "defaultOverwrite";

    private final Collection<String> keywords;

    private final TypeMappings typeMappings = new TypeMappings();

    private final SerializerConfig defaultSerializerConfig;

    private final Serializer dtoSerializer;

    private final Serializer embeddableSerializer;

    private final Serializer entitySerializer;

    private final Serializer supertypeSerializer;

    private String namePrefix = "Q";

    private String nameSuffix = "";

    private final Map<String, SerializerConfig> packageToConfig = new HashMap<String, SerializerConfig>();

    protected final Class<? extends Annotation> entityAnn;

    @Nullable
    protected final Class<? extends Annotation> entitiesAnn, superTypeAnn, embeddedAnn, embeddableAnn, skipAnn;

    private final Map<String, SerializerConfig> typeToConfig = new HashMap<String, SerializerConfig>();

    private final Map<Class<?>, Class<? extends Expression<?>>> customTypes = new HashMap<Class<?>, Class<? extends Expression<?>>>();
    
    private boolean useFields = true, useGetters = true, defaultOverwrite = false;
    
    public DefaultConfiguration(
            RoundEnvironment roundEnv,
            Map<String, String> options,
            Collection<String> keywords,
            @Nullable Class<? extends Annotation> entitiesAnn,
            Class<? extends Annotation> entityAnn,
            @Nullable Class<? extends Annotation> superTypeAnn,
            @Nullable Class<? extends Annotation> embeddableAnn,
            @Nullable Class<? extends Annotation> embeddedAnn,
            @Nullable Class<? extends Annotation> skipAnn) {
        this.keywords = keywords;
        this.dtoSerializer = new ProjectionSerializer(typeMappings);
        this.embeddableSerializer = new EmbeddableSerializer(typeMappings, keywords);
        this.entitySerializer = new EntitySerializer(typeMappings, keywords);
        this.supertypeSerializer = new SupertypeSerializer(typeMappings, keywords);
        this.entitiesAnn = entitiesAnn;
        this.entityAnn = Assert.notNull(entityAnn,"entityAnn");
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.embeddedAnn = embeddedAnn;
        this.skipAnn = skipAnn;
        for (Element element : roundEnv.getElementsAnnotatedWith(Config.class)){
            Config querydslConfig = element.getAnnotation(Config.class);
            SerializerConfig config = SimpleSerializerConfig.getConfig(querydslConfig);
            if (element instanceof PackageElement){
                PackageElement packageElement = (PackageElement)element;
                packageToConfig.put(packageElement.getQualifiedName().toString(), config);
            }else if (element instanceof TypeElement){
                TypeElement typeElement = (TypeElement)element;
                typeToConfig.put(typeElement.getQualifiedName().toString(), config);
            }
        }
        boolean entityAccessors = false;
        boolean listAccessors = false;
        boolean mapAccessors = false;
        boolean createDefaultVariable = true;
        if (options.containsKey(QUERYDSL_ENTITY_ACCESSORS)){
            entityAccessors = Boolean.valueOf(options.get(QUERYDSL_ENTITY_ACCESSORS));
        }
        if (options.containsKey(QUERYDSL_LIST_ACCESSORS)){
            listAccessors = Boolean.valueOf(options.get(QUERYDSL_LIST_ACCESSORS));
        }
        if (options.containsKey(QUERYDSL_MAP_ACCESSORS)){
            mapAccessors = Boolean.valueOf(options.get(QUERYDSL_MAP_ACCESSORS));
        }
        if (options.containsKey(QUERYDSL_CREATE_DEFAULT_VARIABLE)){
            createDefaultVariable = Boolean.valueOf(options.get(QUERYDSL_CREATE_DEFAULT_VARIABLE));
        }
        if (options.containsKey(DEFAULT_OVERWRITE)){
            defaultOverwrite = Boolean.valueOf(options.get(DEFAULT_OVERWRITE));
        }

        defaultSerializerConfig = new SimpleSerializerConfig(entityAccessors, listAccessors, mapAccessors, createDefaultVariable);

    }

    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements){
        if (useFields){
            if (useGetters){
                return VisitorConfig.ALL;
            }else{
                return VisitorConfig.FIELDS_ONLY;
            }
        }else if (useGetters){
            return VisitorConfig.METHODS_ONLY;
        }else{
            return VisitorConfig.NONE;
        }
    }

    @Override
    public Serializer getDTOSerializer() {
        return dtoSerializer;
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getEntitiesAnnotation() {
        return entitiesAnn;
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getEmbeddableAnnotation() {
        return embeddableAnn;
    }

    @Override
    public Serializer getEmbeddableSerializer() {
        return embeddableSerializer;
    }

    @Override
    public Class<? extends Annotation> getEntityAnnotation() {
        return entityAnn;
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getEmbeddedAnnotation() {
        return embeddedAnn;
    }

    @Override
    public Serializer getEntitySerializer() {
        return entitySerializer;
    }

    @Override
    public String getNamePrefix() {
        return namePrefix;
    }

    @Override
    public SerializerConfig getSerializerConfig(EntityType entityType) {
        if (typeToConfig.containsKey(entityType.getFullName())){
            return typeToConfig.get(entityType.getFullName());
        }else if (packageToConfig.containsKey(entityType.getPackageName())){
            return packageToConfig.get(entityType.getPackageName());
        }else{
            return defaultSerializerConfig;
        }
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getSkipAnnotation() {
        return skipAnn;
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getSuperTypeAnnotation() {
        return superTypeAnn;
    }

    @Override
    public Serializer getSupertypeSerializer() {
        return supertypeSerializer;
    }

    @Override
    public boolean isBlockedField(VariableElement field) {
        if (field.getAnnotation(QueryType.class) != null){
            return false;
        }else{
            return field.getAnnotation(skipAnn) != null
            || field.getModifiers().contains(Modifier.TRANSIENT)
            || field.getModifiers().contains(Modifier.STATIC);
        }
    }

    @Override
    public boolean isBlockedGetter(ExecutableElement getter){
        if (getter.getAnnotation(QueryType.class) != null){
            return false;
        }else{
            return getter.getAnnotation(skipAnn) != null
                || getter.getModifiers().contains(Modifier.STATIC);
        }
    }

    @Override
    public boolean isDefaultOverwrite() {
        return defaultOverwrite;
    }

    @Override
    public boolean isUseFields() {
        return useFields;
    }

    @Override
    public boolean isUseGetters() {
        return useGetters;
    }

    @Override
    public boolean isValidConstructor(ExecutableElement constructor) {
        return constructor.getModifiers().contains(Modifier.PUBLIC)
            && constructor.getAnnotation(QueryProjection.class) != null
            && !constructor.getParameters().isEmpty();
    }

    @Override
    public boolean isValidField(VariableElement field) {
        if (field.getAnnotation(QueryType.class) != null){
            return true;
        }else{
            return field.getAnnotation(skipAnn) == null
                && !field.getModifiers().contains(Modifier.TRANSIENT)
                && !field.getModifiers().contains(Modifier.STATIC);
        }
    }

    @Override
    public boolean isValidGetter(ExecutableElement getter){
        if (getter.getAnnotation(QueryType.class) != null){
            return true;
        }else{
            return getter.getAnnotation(skipAnn) == null
                && !getter.getModifiers().contains(Modifier.STATIC);
        }
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setUseFields(boolean b){
        this.useFields = b;
    }

    public void setUseGetters(boolean b) {
        this.useGetters = b;
    }

    @Override
    public TypeMappings getTypeMappings() {
        return typeMappings;
    }

    @Override
    public Collection<String> getKeywords(){
        return keywords;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }
    
    public <T> void addCustomType(Class<T> type, Class<? extends Expression<T>> queryType){
        customTypes.put(type, queryType);
    }

    @Override
    public Map<Class<?>, Class<? extends Expression<?>>> getCustomTypes() {
        return Collections.unmodifiableMap(customTypes);
    }

}
