/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt.jdo;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.mysema.query.apt.APTException;
import com.mysema.query.apt.DefaultConfiguration;
import com.mysema.query.apt.Processor;

/**
 * AnnotationProcessor for JDO which takes @PersistenceCapable, @EmbeddedOnly and @NotPersistent into account
 * 
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JDOAnnotationProcessor extends AbstractProcessor{

    private Class<? extends Annotation> entity, embeddable, skip;

    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
            entity = (Class)Class.forName("javax.jdo.annotations.PersistenceCapable");
            embeddable = (Class)Class.forName("javax.jdo.annotations.EmbeddedOnly");
            skip = (Class)Class.forName("javax.jdo.annotations.NotPersistent");

            DefaultConfiguration configuration = new DefaultConfiguration(roundEnv, processingEnv.getOptions(), null, entity, null, embeddable, null, skip);
            configuration.setUseGetters(false);
            Processor processor = new Processor(processingEnv, roundEnv, configuration);
            processor.process();
            return true;

        } catch (ClassNotFoundException e) {
            throw new APTException(e.getMessage(), e);
        }
    }

    protected DefaultConfiguration createConfiguration(RoundEnvironment roundEnv) throws ClassNotFoundException {
        return new JDOConfiguration(roundEnv, processingEnv.getOptions(), entity, null, embeddable, skip);
    }

}
