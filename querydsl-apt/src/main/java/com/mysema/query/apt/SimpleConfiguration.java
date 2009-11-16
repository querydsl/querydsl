/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.codegen.DTOSerializer;
import com.mysema.query.codegen.EmbeddableSerializer;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SupertypeSerializer;

/**
 * @author tiwe
 *
 */
public class SimpleConfiguration implements Configuration {
    
    private String namePrefix = "Q";
    
    private final Serializer entitySerializer = new EntitySerializer();
    
    private final Serializer supertypeSerializer = new SupertypeSerializer();
    
    private final Serializer embeddableSerializer = new EmbeddableSerializer();
    
    private final Serializer dtoSerializer = new DTOSerializer();
    
    protected final Class<? extends Annotation> entityAnn, superTypeAnn, embeddableAnn, skipAnn;
    
    private boolean useFields = true, useGetters = true;
    
    public SimpleConfiguration(
            Class<? extends Annotation> entityAnn, 
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) {
        this.entityAnn = Assert.notNull(entityAnn);
        this.superTypeAnn = superTypeAnn;
        this.embeddableAnn = embeddableAnn;
        this.skipAnn = skipAnn;             
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getConfig(javax.lang.model.element.TypeElement, java.util.List)
     */
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
    
    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#isValidConstructor(javax.lang.model.element.ExecutableElement)
     */
    public boolean isValidConstructor(ExecutableElement constructor) {
        return constructor.getModifiers().contains(Modifier.PUBLIC)
            && constructor.getAnnotation(QueryProjection.class) != null
            && !constructor.getParameters().isEmpty();
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#isValidField(javax.lang.model.element.VariableElement)
     */
    public boolean isValidField(VariableElement field) {
        return field.getAnnotation(skipAnn) == null
            && !field.getModifiers().contains(Modifier.TRANSIENT) 
            && !field.getModifiers().contains(Modifier.STATIC);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#isValidGetter(javax.lang.model.element.ExecutableElement)
     */
    public boolean isValidGetter(ExecutableElement getter){
        return getter.getAnnotation(skipAnn) == null
            && !getter.getModifiers().contains(Modifier.STATIC);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getEntityAnn()
     */
    public Class<? extends Annotation> getEntityAnn() {
        return entityAnn;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getSuperTypeAnn()
     */
    public Class<? extends Annotation> getSuperTypeAnn() {
        return superTypeAnn;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getEmbeddableAnn()
     */
    public Class<? extends Annotation> getEmbeddableAnn() {
        return embeddableAnn;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getSkipAnn()
     */
    public Class<? extends Annotation> getSkipAnn() {
        return skipAnn;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#setUseGetters(boolean)
     */
    public void setUseGetters(boolean b) {
        this.useGetters = b;        
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#setUseFields(boolean)
     */
    public void setUseFields(boolean b){
        this.useFields = b;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getNamePrefix()
     */
    public String getNamePrefix() {
        return namePrefix;
    }
       

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getEntitySerializer()
     */
    public Serializer getEntitySerializer() {
        return entitySerializer;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getSupertypeSerializer()
     */
    public Serializer getSupertypeSerializer() {
        return supertypeSerializer;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getEmbeddableSerializer()
     */
    public Serializer getEmbeddableSerializer() {
        return embeddableSerializer;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#isUseFields()
     */
    public boolean isUseFields() {
        return useFields;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#isUseGetters()
     */
    public boolean isUseGetters() {
        return useGetters;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#setNamePrefix(java.lang.String)
     */
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.apt.Configuration#getDTOSerializer()
     */
    public Serializer getDTOSerializer() {
        return dtoSerializer;
    }
    
}
