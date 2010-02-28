/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

/**
 * @author tiwe
 *
 */
public interface CodeWriter extends Appendable{
    
    CodeWriter annotation(Annotation annotation) throws IOException;

    CodeWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException;
    
    <T> CodeWriter beginConstructor(Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter beginConstructor(String... params) throws IOException;

    CodeWriter beginInterface(String simpleName, String... interfaces) throws IOException;

    CodeWriter beginLine(String... segments) throws IOException;

    <T> CodeWriter beginMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException;
    
    CodeWriter beginMethod(String returnType, String methodName, String... args) throws IOException;
    
    <T> CodeWriter beginStaticMethod(String type, String name, Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter end() throws IOException;

    CodeWriter imports(Class<?>... imports) throws IOException;

    CodeWriter imports(Package... imports) throws IOException;

    CodeWriter javadoc(String... lines) throws IOException;

    String join(String prefix, String suffix, Iterable<String> args);

    String join(String prefix, String suffix, String... args);

    CodeWriter line(String... segments) throws IOException;

    CodeWriter nl() throws IOException;

    CodeWriter packageDecl(String packageName) throws IOException;

    CodeWriter privateStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter protectedField(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name, String value) throws IOException;

    CodeWriter publicStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter staticimports(Class<?>... imports) throws IOException;

    CodeWriter suppressWarnings(String type) throws IOException;

}