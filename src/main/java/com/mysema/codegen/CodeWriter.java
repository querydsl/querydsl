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

    /**
     * @param annotation
     * @return
     * @throws IOException
     */
    CodeWriter annotation(Annotation annotation) throws IOException;

    /**
     * @param annotation
     * @return
     * @throws IOException
     */
    CodeWriter annotation(Class<? extends Annotation> annotation) throws IOException;

    /**
     * @param simpleName
     * @return
     * @throws IOException
     */
    CodeWriter beginClass(String simpleName) throws IOException;

    /**
     * @param simpleName
     * @param superClass
     * @param interfaces
     * @return
     * @throws IOException
     */
    CodeWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException;

    /**
     * @param <T>
     * @param params
     * @param transformer
     * @return
     * @throws IOException
     */
    <T> CodeWriter beginConstructor(Collection<T> params, Transformer<T, String> transformer) throws IOException;

    /**
     * @param params
     * @return
     * @throws IOException
     */
    CodeWriter beginConstructor(String... params) throws IOException;

    /**
     * @param simpleName
     * @param interfaces
     * @return
     * @throws IOException
     */
    CodeWriter beginInterface(String simpleName, String... interfaces) throws IOException;

    /**
     * @param <T>
     * @param returnType
     * @param methodName
     * @param parameters
     * @param transformer
     * @return
     * @throws IOException
     */
    <T> CodeWriter beginPublicMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException;

    /**
     * @param returnType
     * @param methodName
     * @param args
     * @return
     * @throws IOException
     */
    CodeWriter beginPublicMethod(String returnType, String methodName, String... args) throws IOException;

    /**
     * @param <T>
     * @param type
     * @param name
     * @param params
     * @param transformer
     * @return
     * @throws IOException
     */
    <T> CodeWriter beginStaticMethod(String type, String name, Collection<T> params, Transformer<T, String> transformer) throws IOException;

    /**
     * @param returnType
     * @param methodName
     * @param args
     * @return
     * @throws IOException
     */
    CodeWriter beginStaticMethod(String returnType, String methodName, String... args) throws IOException;

    /**
     * @return
     * @throws IOException
     */
    CodeWriter end() throws IOException;

    /**
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    CodeWriter field(String type, String name) throws IOException;

    /**
     * @param imports
     * @return
     * @throws IOException
     */
    CodeWriter imports(Class<?>... imports) throws IOException;

    /**
     * @param imports
     * @return
     * @throws IOException
     */
    CodeWriter imports(Package... imports) throws IOException;

    /**
     * @param lines
     * @return
     * @throws IOException
     */
    CodeWriter javadoc(String... lines) throws IOException;

    /**
     * @param segments
     * @return
     * @throws IOException
     */
    CodeWriter line(String... segments) throws IOException;

    /**
     * @return
     * @throws IOException
     */
    CodeWriter nl() throws IOException;

    /**
     * @param packageName
     * @return
     * @throws IOException
     */
    CodeWriter packageDecl(String packageName) throws IOException;
    
    /**
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    CodeWriter privateField(String type, String name) throws IOException;
    
    /**
     * @param type
     * @param name
     * @param value
     * @return
     * @throws IOException
     */
    CodeWriter privateStaticFinal(String type, String name, String value) throws IOException;
    
    /**
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    CodeWriter protectedField(String type, String name) throws IOException;

    /**
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    CodeWriter publicField(String type, String name) throws IOException;

    /**
     * @param type
     * @param name
     * @return
     * @throws IOException
     */
    CodeWriter publicFinal(String type, String name) throws IOException;

    /**
     * @param type
     * @param name
     * @param value
     * @return
     * @throws IOException
     */
    CodeWriter publicFinal(String type, String name, String value) throws IOException;

    /**
     * @param type
     * @param name
     * @param value
     * @return
     * @throws IOException
     */
    CodeWriter publicStaticFinal(String type, String name, String value) throws IOException;

    /**
     * @param imports
     * @return
     * @throws IOException
     */
    CodeWriter staticimports(Class<?>... imports) throws IOException;

    /**
     * @param type
     * @return
     * @throws IOException
     */
    CodeWriter suppressWarnings(String type) throws IOException;

}