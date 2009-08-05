package com.mysema.query.hql.apt;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
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
public class JPAAnnotationProcessor extends AbstractProcessor{
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
            Class<? extends Annotation> entity = (Class)Class.forName("javax.persistence.Entity");
            Class<? extends Annotation> superType = (Class)Class.forName("javax.persistence.MappedSuperclass");
            Class<? extends Annotation> embeddable = (Class)Class.forName("javax.persistence.Embeddable");
            Class<? extends Annotation> dtoAnnotation = Projection.class;
            Processor p = new Processor(processingEnv, entity, superType, embeddable, dtoAnnotation, "Q");
            p.process(roundEnv);
            return true;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }        
    }       
    
}
