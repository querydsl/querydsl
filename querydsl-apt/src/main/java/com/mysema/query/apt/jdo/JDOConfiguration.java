package com.mysema.query.apt.jdo;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.processing.RoundEnvironment;

import com.mysema.query.apt.DefaultConfiguration;

/**
 * Configuration for {@link JDOAnnotationProcessor}
 * 
 * @author tiwe
 * @see JDOAnnotationProcessor
 *
 */
public class JDOConfiguration extends DefaultConfiguration{

    private static final List<String> keywords = Arrays.asList(
            "AS","ASC",
            "ASCENDING","AVG",
            "BY","COUNT",
            "DESC","DESCENDING",
            "DISTINCT","EXCLUDE",
            "FROM","GROUP",
            "HAVING","INTO",
            "MAX","MIN",
            "ORDER","PARAMETERS",
            "RANGE","SELECT",
            "SUBCLASSES","SUM",
            "UNIQUE","VARIABLES",
            "WHERE");

    public JDOConfiguration(RoundEnvironment roundEnv,Map<String,String> options,
            Class<? extends Annotation> entityAnn,
            @Nullable Class<? extends Annotation> superTypeAnn,
            Class<? extends Annotation> embeddableAnn,
            Class<? extends Annotation> skipAnn) throws ClassNotFoundException {
        super(roundEnv, options, null, entityAnn, superTypeAnn, embeddableAnn, skipAnn);
    }

    @Override
    public Collection<String> getKeywords(){
        return keywords;
    }

}
