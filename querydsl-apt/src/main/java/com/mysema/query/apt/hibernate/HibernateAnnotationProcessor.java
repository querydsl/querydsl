package com.mysema.query.apt.hibernate;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

/**
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor{
    
    @Override
    protected DefaultConfiguration createConfiguration(RoundEnvironment roundEnv) throws ClassNotFoundException {
        return new HibernateConfiguration(roundEnv, entity, superType, embeddable, skip);
    } 

}
