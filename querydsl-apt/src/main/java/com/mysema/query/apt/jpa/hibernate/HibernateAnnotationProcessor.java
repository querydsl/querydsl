package com.mysema.query.apt.jpa.hibernate;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

/**
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateAnnotationProcessor extends JPAAnnotationProcessor{
    
    @Override
    protected Configuration createConfiguration() throws ClassNotFoundException {
        return new HibernateConfiguration(entity, superType, embeddable, dto, skip);
    } 

}
