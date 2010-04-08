/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;


/**
 * EvaluatorFactory is a factory implementation for creating Evaluator instances
 * 
 * @author tiwe
 * 
 */
public class EvaluatorFactory {

    private final MemFileManager fileManager;
    
    private final String classpath;

    private final List<String> compilationOptions;

    private final JavaCompiler compiler;

    private final ClassLoader loader;

    public EvaluatorFactory(URLClassLoader parent){
        this(parent, ToolProvider.getSystemJavaCompiler());
    }
    
    public EvaluatorFactory(URLClassLoader parent, JavaCompiler compiler) {
        this.fileManager = new MemFileManager(compiler.getStandardFileManager(null, null, null));
        this.compiler = compiler;                        
        this.classpath = SimpleCompiler.getClassPath(parent);
        this.loader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
        this.compilationOptions = Arrays.asList("-classpath", classpath, "-g:none");
    }

    private void compile(String source, Class<?> projectionType,
            String[] names, Class<?>[] types, String id) throws IOException {
        // create source
        StringWriter writer = new StringWriter();
        JavaWriter javaw = new JavaWriter(writer);
        javaw.beginClass(id, null);
        String[] params = new String[names.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = ClassUtils.getName(types[i]) + " " + names[i];
        }

        javaw.beginStaticMethod(ClassUtils.getName(projectionType), "eval", params);
        javaw.line("return ", source, ";");
        javaw.end();
        javaw.end();

        // compile
        SimpleJavaFileObject javaFileObject = new MemSourceFileObject(id, writer.toString());
        Writer out = new StringWriter();

        CompilationTask task = compiler.getTask(
                out, 
                fileManager,
                null,                     
                compilationOptions, 
                null, 
                Collections.singletonList(javaFileObject));
        if (!task.call().booleanValue()) {
            throw new CodegenException("Compilation of " + source + " failed.\n" + out.toString());
        }

    }

    public <T> Evaluator<T> createEvaluator(String source,
            Class<? extends T> projectionType, String[] names, Class<?>[] types) {

        try {
            String id = toId(source, projectionType, types);
            Class<?> clazz;
            try{
                clazz = loader.loadClass(id);
            }catch(ClassNotFoundException e){
                compile(source, projectionType, names, types, id);
                // reload
                clazz = loader.loadClass(id);
            }

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
            throw new CodegenException(e);
        } catch (SecurityException e) {
            throw new CodegenException(e);
        } catch (NoSuchMethodException e) {
            throw new CodegenException(e);
        } catch (UnsupportedEncodingException e) {
            throw new CodegenException(e);
        } catch (IOException e) {
            throw new CodegenException(e);
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

}
