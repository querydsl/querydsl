package com.mysema.query.apt.jpa.hibernate;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.Processor;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class HibernateAnnotationProcessor extends AbstractProcessor{
    
    private Class<? extends Annotation> entity, superType, embeddable, dto, skip;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
            entity = (Class)Class.forName("javax.persistence.Entity");
            superType = (Class)Class.forName("javax.persistence.MappedSuperclass");
            embeddable = (Class)Class.forName("javax.persistence.Embeddable");
            dto = QueryProjection.class;
            skip = (Class)Class.forName("javax.persistence.Transient");
            
            Configuration configuration = new HibernateConfiguration(entity, superType, embeddable, dto, skip);
            Processor processor = new Processor(processingEnv, configuration);
            processor.process(roundEnv);
            return true;
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }        
    } 

}
