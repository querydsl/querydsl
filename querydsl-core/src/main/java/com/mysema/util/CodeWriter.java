package com.mysema.util;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

/**
 * @author tiwe
 *
 */
public interface CodeWriter extends Appendable{
    
    String join(String prefix, String suffix, String... args);
    
    String join(String prefix, String suffix, Iterable<String> args);

    CodeWriter beginClass(String simpleName, String superClass, String... interfaces) throws IOException;

    <T> CodeWriter beginConstructor(Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter beginConstructor(String... params) throws IOException;

    <T> CodeWriter beginMethod(String returnType, String methodName, Collection<T> parameters, Transformer<T, String> transformer) throws IOException;
    
    CodeWriter beginMethod(String returnType, String methodName, String... args) throws IOException;

    <T> CodeWriter beginStaticMethod(String type, String name, Collection<T> params, Transformer<T, String> transformer) throws IOException;

    CodeWriter end() throws IOException;

    CodeWriter imports(Class<?>... imports) throws IOException;

    CodeWriter imports(Package... imports) throws IOException;

    CodeWriter staticimports(Class<?>... imports) throws IOException;

    CodeWriter javadoc(String... lines) throws IOException;

    CodeWriter line(String... segments) throws IOException;

    CodeWriter lines(String... lines) throws IOException;

    CodeWriter nl() throws IOException;

    CodeWriter packageDecl(String packageName) throws IOException;

    CodeWriter privateStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter protectedField(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name) throws IOException;

    CodeWriter publicFinal(String type, String name, String value) throws IOException;

    CodeWriter publicStaticFinal(String type, String name, String value) throws IOException;

    CodeWriter suppressWarnings(String type) throws IOException;

}