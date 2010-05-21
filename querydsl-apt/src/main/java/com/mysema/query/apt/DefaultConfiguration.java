/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
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
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.annotations.QuerydslConfig;
import com.mysema.query.codegen.DTOSerializer;
import com.mysema.query.codegen.EmbeddableSerializer;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SupertypeSerializer;
import com.mysema.query.codegen.TypeMappings;

/**
 * DefaultConfiguration is a simple implementation of the Configuration interface
 * 
 * @author tiwe
 *
 */
public class DefaultConfiguration implements Configuration {
    
    private final TypeMappings typeMappings = new TypeMappings();
    
    private final Serializer dtoSerializer = new DTOSerializer(typeMappings);
    
    private final Serializer embeddableSerializer = new EmbeddableSerializer(typeMappings);
    
    protected final Class<? extends Annotation> entityAnn;
    
    private final Serializer entitySerializer = new EntitySerializer(typeMappings);
    
    private String namePrefix = "Q";
    
    private final Map<String,SerializerConfig> packageToConfig = new HashMap<String,SerializerConfig>();
    
    @Nullable
    protected final Class<? extends Annotation> superTypeAnn, embeddableAnn, skipAnn;
    
    private final Serializer supertypeSerializer = new SupertypeSerializer(typeMappings);
    
    private final Map<String,SerializerConfig> typeToConfig = new HashMap<String,SerializerConfig>();
    
    private final SerializerConfig defaultSerializerConfig;
    
    private boolean useFields = true, useGetters = true;
    
    public DefaultConfiguration(
            RoundEnvironment roundEnv,
            Map<String, String> options, 
            Class<? extends Annotation> entityAnn, 
            @Nullable Class<? extends Annotation> superTypeAnn,
            @Nullable Class<? extends Annotation> embeddableAnn,
            @Nullable Class<? extends Annotation> skipAnn) {
        this.entityAnn = Assert.notNull(entityAnn,"entityAnn");
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.skipAnn = skipAnn;
        for (Element element : roundEnv.getElementsAnnotatedWith(QuerydslConfig.class)){
            QuerydslConfig querydslConfig = element.getAnnotation(QuerydslConfig.class);
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
        if (options.containsKey("querydsl.entityAccessors")){
            entityAccessors = Boolean.valueOf(options.get("querydsl.entityAccessors"));
        }
        if (options.containsKey("querydsl.listAccessors")){
            listAccessors = Boolean.valueOf(options.get("querydsl.listAccessors"));
        }
        if (options.containsKey("querydsl.mapAccessors")){
            mapAccessors = Boolean.valueOf(options.get("querydsl.mapAccessors"));
        }
        if (options.containsKey("querydsl.createDefaultVariable")){
            createDefaultVariable = Boolean.valueOf(options.get("querydsl.createDefaultVariable"));
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
    public Class<? extends Annotation> getEmbeddableAnn() {
        return embeddableAnn;
    }

    @Override
    public Serializer getEmbeddableSerializer() {
        return embeddableSerializer;
    }
    
    @Override
    public Class<? extends Annotation> getEntityAnn() {
        return entityAnn;
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
    public SerializerConfig getSerializerConfig(EntityType model) {
        if (typeToConfig.containsKey(model.getFullName())){
            return typeToConfig.get(model.getFullName());
        }else if (packageToConfig.containsKey(model.getPackageName())){
            return packageToConfig.get(model.getPackageName());
        }else{
            return defaultSerializerConfig;    
        }        
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getSkipAnn() {
        return skipAnn;
    }

    @Override
    @Nullable
    public Class<? extends Annotation> getSuperTypeAnn() {
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

    @Override
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public void setUseFields(boolean b){
        this.useFields = b;
    }
    
    @Override
    public void setUseGetters(boolean b) {
        this.useGetters = b;        
    }

    @Override
    public TypeMappings getTypeMappings() {
        return typeMappings;
    }
    
}
