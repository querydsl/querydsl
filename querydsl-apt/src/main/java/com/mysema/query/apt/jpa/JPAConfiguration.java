/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.jpa;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
 * Configuration for {@link JPAAnnotationProcessor}
 * 
 * @author tiwe
 * @see JPAAnnotationProcessor
 */
public class JPAConfiguration extends DefaultConfiguration {

    private static final Collection<String> KEYWORDS = new HashSet<String>(Arrays.asList(
            "ABS","ALL","AND","ANY","AS","ASC","AVG","BETWEEN",
            "BIT_LENGTH[51]","BOTH","BY","CASE","CHAR_LENGTH",
            "CHARACTER_LENGTH","CLASS",
            "COALESCE","CONCAT","COUNT","CURRENT_DATE","CURRENT_TIME",
            "CURRENT_TIMESTAMP",
            "DELETE","DESC","DISTINCT","ELSE","EMPTY","END","ENTRY",
            "ESCAPE","EXISTS","FALSE","FETCH",
            "FROM","GROUP","HAVING","IN","INDEX","INNER","IS","JOIN",
            "KEY","LEADING","LEFT","LENGTH","LIKE",
            "LOCATE","LOWER","MAX","MEMBER","MIN","MOD","NEW","NOT",
            "NULL","NULLIF","OBJECT","OF","OR",
            "ORDER","OUTER","POSITION","SELECT","SET","SIZE","SOME",
            "SQRT","SUBSTRING","SUM","THEN",
            "TRAILING","TRIM","TRUE","TYPE","UNKNOWN","UPDATE","UPPER",
            "VALUE","WHEN","WHERE"));

    private List<Class<? extends Annotation>> annotations;

    public JPAConfiguration(RoundEnvironment roundEnv,Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(roundEnv, options, KEYWORDS, null, entityAnn, superTypeAnn, embeddableAnn, null, skipAnn);
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
