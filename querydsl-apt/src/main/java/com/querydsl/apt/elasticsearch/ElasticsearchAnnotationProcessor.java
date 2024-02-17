package com.querydsl.apt.elasticsearch;

import com.querydsl.apt.AbstractQuerydslProcessor;
import com.querydsl.apt.Configuration;
import com.querydsl.apt.DefaultConfiguration;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntities;
import com.querydsl.core.annotations.QuerySupertype;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.annotation.Annotation;
import java.util.Collections;

/**
 * Annotation processor to create Querydsl query types for Elasticsearch annotated classes.
 */
@SupportedAnnotationTypes({"com.querydsl.core.annotations.*", "org.springframework.data.elasticsearch.annotations.*"})
public class ElasticsearchAnnotationProcessor extends AbstractQuerydslProcessor {
    @Override
    protected Configuration createConfiguration(final RoundEnvironment roundEnvironment) {
        // elasticsearch annotation
        Class<? extends Annotation> document = Document.class;
        Class<? extends Annotation> skip = Transient.class;

        // query-dsl annotation
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> superType = QuerySupertype.class;
        Class<? extends Annotation> embedded = QueryEmbedded.class;

        return new DefaultConfiguration(processingEnv, roundEnvironment,
                Collections.emptySet(),
                entities, document, superType, null, embedded, skip);
    }
}
