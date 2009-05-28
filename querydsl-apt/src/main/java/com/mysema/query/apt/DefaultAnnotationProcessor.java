/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.mysema.query.apt.impl.DefaultHandler;

@SupportedAnnotationTypes(value= {"com.mysema.query.annotations.Entity"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DefaultAnnotationProcessor extends AbstractProcessor{
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        DefaultHandler handler = new DefaultHandler();
        for (TypeElement type : annotations){
            handler.processClasses(roundEnv.getElementsAnnotatedWith(type), roundEnv);
        }        
        return true;
    }       

}
