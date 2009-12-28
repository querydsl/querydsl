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
import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.codegen.SupertypeSerializer;

/**
 * SimpleConfiguration is a simple implementation of the Configuration interface
 * 
 * @author tiwe
 *
 */
public class SimpleConfiguration implements Configuration {
    
    private String namePrefix = "Q";
    
    private final Serializer entitySerializer = new EntitySerializer();
    
    private final Serializer supertypeSerializer = new SupertypeSerializer();
    
    private final Serializer embeddableSerializer = new EmbeddableSerializer();
    
    private final Serializer dtoSerializer = new DTOSerializer();
    
    protected final Class<? extends Annotation> entityAnn, embeddableAnn, skipAnn;
    
    @Nullable
    protected final Class<? extends Annotation> superTypeAnn;
    
    private final Map<String,SerializerConfig> packageToConfig = new HashMap<String,SerializerConfig>();
    
    private final Map<String,SerializerConfig> typeToConfig = new HashMap<String,SerializerConfig>();
    
    private boolean useFields = true, useGetters = true;
    
    public SimpleConfiguration(
            RoundEnvironment roundEnv,
            Class<? extends Annotation> entityAnn, 
            @Nullable
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) {
        this.entityAnn = Assert.notNull(entityAnn);
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.skipAnn = skipAnn;
        for (Element element : roundEnv.getElementsAnnotatedWith(QuerydslConfig.class)){
            SerializerConfig config = SimpleSerializerConfig.getConfig(element.getAnnotation(QuerydslConfig.class));
            if (element instanceof PackageElement){
                PackageElement packageElement = (PackageElement)element;
                packageToConfig.put(packageElement.getQualifiedName().toString(), config);
            }else if (element instanceof TypeElement){
                TypeElement typeElement = (TypeElement)element;                
                typeToConfig.put(typeElement.getQualifiedName().toString(), config);
            }
        }
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
    public Class<? extends Annotation> getEntityAnn() {
        return entityAnn;
    }

    @Override
    public Class<? extends Annotation> getSuperTypeAnn() {
        return superTypeAnn;
    }

    @Override
    public Class<? extends Annotation> getEmbeddableAnn() {
        return embeddableAnn;
    }

    @Override
    public Class<? extends Annotation> getSkipAnn() {
        return skipAnn;
    }

    @Override
    public void setUseGetters(boolean b) {
        this.useGetters = b;        
    }

    @Override
    public void setUseFields(boolean b){
        this.useFields = b;
    }

    @Override
    public String getNamePrefix() {
        return namePrefix;
    }
 
    @Override
    public Serializer getEntitySerializer() {
        return entitySerializer;
    }

    @Override
    public Serializer getSupertypeSerializer() {
        return supertypeSerializer;
    }

    @Override
    public Serializer getEmbeddableSerializer() {
        return embeddableSerializer;
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
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Serializer getDTOSerializer() {
        return dtoSerializer;
    }
    
    @Override
    public SerializerConfig getSerializerConfig(EntityModel model) {
        if (typeToConfig.containsKey(model.getFullName())){
            return typeToConfig.get(model.getFullName());
        }else if (packageToConfig.containsKey(model.getPackageName())){
            return packageToConfig.get(model.getPackageName());
        }else{
            return SimpleSerializerConfig.DEFAULT;    
        }        
    }
    
}
