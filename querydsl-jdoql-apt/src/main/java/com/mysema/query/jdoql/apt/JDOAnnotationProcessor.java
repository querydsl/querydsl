package com.mysema.query.jdoql.apt;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.mysema.query.annotations.Projection;
import com.mysema.query.apt.Processor;

/**
 * @author tiwe
 *
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JDOAnnotationProcessor extends AbstractProcessor{
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
        Class<? extends Annotation> entity = PersistenceCapable.class;
        Class<? extends Annotation> superType = null; // ?!?
        Class<? extends Annotation> embeddable = EmbeddedOnly.class;
        Class<? extends Annotation> dtoAnnotation = Projection.class;
        Processor p = new Processor(processingEnv, entity, superType, embeddable, dtoAnnotation, "Q");
        p.skipGetters().process(roundEnv);
        return true;
    }       
    
}