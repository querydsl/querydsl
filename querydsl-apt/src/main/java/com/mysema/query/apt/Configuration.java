package com.mysema.query.apt;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.mysema.query.codegen.Serializer;

public interface Configuration {

    public VisitorConfig getConfig(TypeElement e,
            List<? extends Element> elements);

    public boolean isValidConstructor(ExecutableElement constructor);

    public boolean isValidField(VariableElement field);

    public boolean isValidGetter(ExecutableElement getter);

    public Class<? extends Annotation> getEntityAnn();

    public Class<? extends Annotation> getSuperTypeAnn();

    public Class<? extends Annotation> getEmbeddableAnn();

    public Class<? extends Annotation> getSkipAnn();

    public void setUseGetters(boolean b);

    public void setUseFields(boolean b);

    public String getNamePrefix();

    public Serializer getEntitySerializer();

    public Serializer getSupertypeSerializer();

    public Serializer getEmbeddableSerializer();

    public boolean isUseFields();

    public boolean isUseGetters();

    public void setNamePrefix(String namePrefix);

}