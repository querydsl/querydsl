package com.mysema.query.apt.hibernate;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.mysema.query.apt.SimpleConfiguration;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

/**
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor{
    
    @Override
    protected SimpleConfiguration createConfiguration() throws ClassNotFoundException {
        return new HibernateConfiguration(entity, superType, embeddable, skip);
    } 

}
