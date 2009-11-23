package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.query.codegen.Serializer;

/**
 * Configuration defines the configuration options for APT based Querydsl code generation
 * 
 * @author tiwe
 *
 */
public interface Configuration {

    VisitorConfig getConfig(TypeElement e, List<? extends Element> elements);

    boolean isValidConstructor(ExecutableElement constructor);

    boolean isValidField(VariableElement field);
    
    boolean isBlockedField(VariableElement field);

    boolean isValidGetter(ExecutableElement getter);
    
    boolean isBlockedGetter(ExecutableElement getter);

    Class<? extends Annotation> getEntityAnn();

    Class<? extends Annotation> getSuperTypeAnn();

    Class<? extends Annotation> getEmbeddableAnn();

    Class<? extends Annotation> getSkipAnn();

    void setUseGetters(boolean b);

    void setUseFields(boolean b);

    String getNamePrefix();

    Serializer getEntitySerializer();

    Serializer getSupertypeSerializer();

    Serializer getDTOSerializer();
    
    Serializer getEmbeddableSerializer();

    boolean isUseFields();

    boolean isUseGetters();

    void setNamePrefix(String namePrefix);

}