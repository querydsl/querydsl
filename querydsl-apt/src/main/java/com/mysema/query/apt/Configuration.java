package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.query.codegen.EntityModel;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;

/**
 * Configuration defines the configuration options for APT based Querydsl code generation
 * 
 * @author tiwe
 *
 */
public interface Configuration {

    /**
     * @return
     */
    TypeMappings getTypeMappings();
    
    /**
     * @param e
     * @param elements
     * @return
     */
    VisitorConfig getConfig(TypeElement e, List<? extends Element> elements);

    /**
     * @return
     */
    Serializer getDTOSerializer();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getEmbeddableAnn();
    
    /**
     * @return
     */
    Serializer getEmbeddableSerializer();

    /**
     * @return
     */
    Class<? extends Annotation> getEntityAnn();
    
    /**
     * @return
     */
    Serializer getEntitySerializer();

    /**
     * @return
     */
    String getNamePrefix();

    /**
     * @param model
     * @return
     */
    SerializerConfig getSerializerConfig(EntityModel model);

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getSkipAnn();

    /**
     * @return
     */
    @Nullable
    Class<? extends Annotation> getSuperTypeAnn();
    
    /**
     * @return
     */
    Serializer getSupertypeSerializer();

    /**
     * @param field
     * @return
     */
    boolean isBlockedField(VariableElement field);

    /**
     * @param getter
     * @return
     */
    boolean isBlockedGetter(ExecutableElement getter);

    /**
     * @return
     */
    boolean isUseFields();

    /**
     * @return
     */
    boolean isUseGetters();

    /**
     * @param constructor
     * @return
     */
    boolean isValidConstructor(ExecutableElement constructor);

    /**
     * @param field
     * @return
     */
    boolean isValidField(VariableElement field);
    
    /**
     * @param getter
     * @return
     */
    boolean isValidGetter(ExecutableElement getter);

    /**
     * @param namePrefix
     */
    void setNamePrefix(String namePrefix);

    /**
     * @param b
     */
    void setUseFields(boolean b);

    /**
     * @param b
     */
    void setUseGetters(boolean b);

}