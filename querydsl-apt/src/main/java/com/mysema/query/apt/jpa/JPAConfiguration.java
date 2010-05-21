/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.jpa;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.mysema.query.annotations.QueryType;
import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.VisitorConfig;

/**
 * @author tiwe
 *
 */
public class JPAConfiguration extends DefaultConfiguration {
   
    private List<Class<? extends Annotation>> annotations;
    
    public JPAConfiguration(
            RoundEnvironment roundEnv,
            Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(roundEnv, options, entityAnn, superTypeAnn, embeddableAnn, skipAnn);
        this.annotations = getAnnotations();
    }
    
    @SuppressWarnings("unchecked")
    protected List<Class<? extends Annotation>> getAnnotations() throws ClassNotFoundException{
        List<Class<? extends Annotation>> rv = new ArrayList<Class<? extends Annotation>>();
        rv.add(QueryType.class);
        for (String simpleName : Arrays.asList(
                "Column",
                "Embedded",
                "EmbeddedId",
                "GeneratedValue",
                "Id",
                "Version",
                "JoinColumn",
                "ManyToOne",
                "OneToMany",
                "PrimaryKeyJoinColumn")){
            rv.add((Class<? extends Annotation>) Class.forName("javax.persistence."+simpleName));
        }
        return rv;
    }
    
    @Override
    public VisitorConfig getConfig(TypeElement e, List<? extends Element> elements){
        boolean fields = false, methods = false;
        for (Element element : elements){
            if (hasRelevantAnnotation(element)){
                if (element.getKind().equals(ElementKind.FIELD)){
                    fields = true;
                }else if (element.getKind().equals(ElementKind.METHOD)){
                    methods = true;
                }    
            }            
        }    
        if (fields && !methods){
            return VisitorConfig.FIELDS_ONLY;
        }else if (methods && !fields){
            return VisitorConfig.METHODS_ONLY;
        }else{
            return VisitorConfig.ALL;    
        }        
        
    }
    
    private boolean hasRelevantAnnotation(Element element){
        for (Class<? extends Annotation> annotation : annotations){
            if (element.getAnnotation(annotation) != null){
                return true;
            }
        }        
        return false;
    }

}
