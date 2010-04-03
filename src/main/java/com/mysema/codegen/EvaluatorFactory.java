/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaCompiler.CompilationTask;


/**
 * EvaluatorFactory is a factory implementation for creating Evaluator instances
 * 
 * @author tiwe
 * 
 */
public class EvaluatorFactory {

    private final File classDir;

    private final ClassLoader loader;

    private final String classpath;

    private final JavaCompiler compiler;

    private final List<String> compilationOptions;

    public EvaluatorFactory(JavaCompiler compiler, File classDir,
            URLClassLoader parent) {
        try {
            this.compiler = compiler;
            this.classDir = classDir;
            if (!classDir.exists()){
                if (!classDir.mkdirs()){
                    throw new IllegalArgumentException(
                            classDir.getAbsolutePath() + " could not be created");
                }
            }            
            this.classpath = SimpleCompiler.getClassPath(parent);
            this.loader = new URLClassLoader(new URL[] { classDir.toURI().toURL() }, parent);
            this.compilationOptions = Arrays.asList(
                    "-classpath", classpath,
                    "-d", classDir.getAbsolutePath(), 
                    "-g:none");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void compile(String source, Class<?> projectionType,
            String[] names, Class<?>[] types, String id) throws IOException,
            UnsupportedEncodingException {
        // create source
        StringWriter writer = new StringWriter();
        JavaWriter javaw = new JavaWriter(writer);
        javaw.beginClass(id, null);
        String[] params = new String[names.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = toName(types[i]) + " " + names[i];
        }

        javaw.beginStaticMethod(toName(projectionType), "eval", params);
        javaw.line("return ", source, ";");
        javaw.end();
        javaw.end();

        // compile
        try {
            SimpleJavaFileObject javaFileObject = new StringJavaFileObject(id, writer.toString());
            Writer out = new StringWriter();

            CompilationTask task = compiler.getTask(out, null, null,
                    compilationOptions, null, Collections.singletonList(javaFileObject));
            if (!task.call().booleanValue()) {
                throw new RuntimeException("Compilation of " + source + " failed.\n" + out.toString());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }

    }

    protected String toId(String source, Class<?> returnType, Class<?>... types) {
        StringBuilder b = new StringBuilder("Q");
        b.append("_").append(source.hashCode());
        b.append("_").append(returnType.getName().hashCode());
        for (Class<?> type : types) {
            b.append("_").append(type.getName().hashCode());
        }
        return b.toString().replace('-', '0');
    }

    protected String toName(Class<?> cl) {
        if (cl.isArray()) {
            return toName(cl.getComponentType()) + "[]";
        } else if (cl.getPackage() == null || cl.getPackage().getName().equals("java.lang")) {
            return cl.getSimpleName();
        } else {
            return cl.getName().replace('$', '.');
        }
    }

    public <T> Evaluator<T> createEvaluator(String source,
            Class<? extends T> projectionType, String[] names, Class<?>[] types) {

        try {
            String id = toId(source, projectionType, types);
            if (!new File(classDir, id + ".class").exists()) {
                compile(source, projectionType, names, types, id);
            }

            Class<?> clazz = loader.loadClass(id);
            final Method method = clazz.getMethod("eval", types);
            return new Evaluator<T>() {
                @SuppressWarnings("unchecked")
                @Override
                public T evaluate(Object... args) {
                    try {
                        return (T) method.invoke(null, args);
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException(e);
                    } catch (InvocationTargetException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            };
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
