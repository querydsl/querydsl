/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

/**
 * CodeWriter defines an interface for serializing Java source code
 * 
 * @author tiwe
 *
 */
public interface CodeWriter extends Appendable{

    CodeWriter annotation(Annotation annotation) throws IOException;

    CodeWriter annotation(Class<? extends Annotation> annotation) throws IOException;

    CodeWriter beginClass(String simpleName) throws IOException;

    CodeWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException;

    <T> CodeWriter beginConstructor(Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter beginConstructor(String... params) throws IOException;

    CodeWriter beginInterface(String simpleName, String... interfaces) throws IOException;

    JavaWriter beginLine(String... segments) throws IOException;

    <T> CodeWriter beginPublicMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException;

    CodeWriter beginPublicMethod(String returnType, String methodName, String... args) throws IOException;

    <T> CodeWriter beginStaticMethod(String type, String name, Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter beginStaticMethod(String returnType, String methodName, String... args) throws IOException;

    CodeWriter end() throws IOException;

    CodeWriter field(String type, String name) throws IOException;

    CodeWriter imports(Class<?>... imports) throws IOException;

    CodeWriter imports(Package... imports) throws IOException;
    
    CodeWriter importClasses(String... classes) throws IOException;
    
    CodeWriter importPackages(String... packages) throws IOException;

    CodeWriter javadoc(String... lines) throws IOException;

    CodeWriter line(String... segments) throws IOException;

    CodeWriter nl() throws IOException;

    CodeWriter packageDecl(String packageName) throws IOException;
    
    CodeWriter privateField(String type, String name) throws IOException;

    CodeWriter privateFinal(String type, String name) throws IOException;

    CodeWriter privateFinal(String type, String name, String value) throws IOException;

    CodeWriter privateStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter protectedField(String type, String name) throws IOException;
    
    CodeWriter protectedFinal(String type, String name) throws IOException;

    CodeWriter protectedFinal(String type, String name, String value) throws IOException;
    
    CodeWriter publicField(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name, String value) throws IOException;

    CodeWriter publicStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter staticimports(Class<?>... imports) throws IOException;

    CodeWriter suppressWarnings(String type) throws IOException;

}