/*
 * Copyright 2010, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;

import com.querydsl.codegen.utils.model.Parameter;
import com.querydsl.codegen.utils.model.Type;

/**
 * CodeWriter defines an interface for serializing Java source code
 * 
 * @author tiwe
 * 
 */
public interface CodeWriter extends Appendable {

    String getRawName(Type type);

    String getGenericName(boolean asArgType, Type type);

    String getClassConstant(String className);

    CodeWriter annotation(Annotation annotation) throws IOException;

    CodeWriter annotation(Class<? extends Annotation> annotation) throws IOException;

    CodeWriter beginClass(Type type) throws IOException;

    CodeWriter beginClass(Type type, Type superClass, Type... interfaces) throws IOException;

    <T> CodeWriter beginConstructor(Collection<T> params, Function<T, Parameter> transformer) throws IOException;

    CodeWriter beginConstructor(Parameter... params) throws IOException;

    CodeWriter beginInterface(Type type, Type... interfaces) throws IOException;

    CodeWriter beginLine(String... segments) throws IOException;

    <T> CodeWriter beginPublicMethod(Type returnType, String methodName, Collection<T> parameters,
            Function<T, Parameter> transformer) throws IOException;

    CodeWriter beginPublicMethod(Type returnType, String methodName, Parameter... args) throws IOException;

    <T> CodeWriter beginStaticMethod(Type type, String name, Collection<T> params,
            Function<T, Parameter> transformer) throws IOException;

    CodeWriter beginStaticMethod(Type returnType, String methodName, Parameter... args) throws IOException;

    CodeWriter end() throws IOException;

    CodeWriter field(Type type, String name) throws IOException;

    CodeWriter imports(Class<?>... imports) throws IOException;

    CodeWriter imports(Package... imports) throws IOException;

    CodeWriter importClasses(String... classes) throws IOException;

    CodeWriter importPackages(String... packages) throws IOException;

    CodeWriter javadoc(String... lines) throws IOException;

    CodeWriter line(String... segments) throws IOException;

    CodeWriter nl() throws IOException;

    CodeWriter packageDecl(String packageName) throws IOException;

    CodeWriter privateField(Type type, String name) throws IOException;

    CodeWriter privateFinal(Type type, String name) throws IOException;

    CodeWriter privateFinal(Type type, String name, String value) throws IOException;

    CodeWriter privateStaticFinal(Type type, String name, String value) throws IOException;

    CodeWriter protectedField(Type type, String name) throws IOException;

    CodeWriter protectedFinal(Type type, String name) throws IOException;

    CodeWriter protectedFinal(Type type, String name, String value) throws IOException;

    CodeWriter publicField(Type type, String name) throws IOException;

    CodeWriter publicField(Type type, String name, String value) throws IOException;

    CodeWriter publicFinal(Type type, String name) throws IOException;

    CodeWriter publicFinal(Type type, String name, String value) throws IOException;

    CodeWriter publicStaticFinal(Type type, String name, String value) throws IOException;

    CodeWriter staticimports(Class<?>... imports) throws IOException;

    CodeWriter suppressWarnings(String type) throws IOException;

    CodeWriter suppressWarnings(String... types) throws IOException;

}