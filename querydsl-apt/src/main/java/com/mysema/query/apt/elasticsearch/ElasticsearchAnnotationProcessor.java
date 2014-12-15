package com.mysema.query.apt.elasticsearch;

import com.mysema.query.annotations.*;
import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.annotation.Annotation;
import java.util.Collections;

/**
 * Annotation processor to create Querydsl query types for Elasticsearch annotated classes
 *
 * @author Kevin Leturc
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*"})
public class ElasticsearchAnnotationProcessor extends AbstractQuerydslProcessor {

    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = QueryEntity.class;
        Class<? extends Annotation> superType = QuerySupertype.class;
        Class<? extends Annotation> embedded = QueryEmbedded.class;
        Class<? extends Annotation> skip = QueryTransient.class;
        return new DefaultConfiguration(roundEnv,
                processingEnv.getOptions(), Collections.<String>emptySet(),
                entities, entity, superType, null, embedded, skip);
    }

}
