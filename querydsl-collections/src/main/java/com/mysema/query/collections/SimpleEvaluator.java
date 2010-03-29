/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

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
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import com.mysema.query.QueryException;
import com.mysema.util.StringJavaFileObject;
import com.mysema.util.JavaWriter;
import com.mysema.util.SimpleCompiler;

/**
 * SimpleEvaluator is a Java Compiler API based implementation of the Evaluator interface
 * 
 * @author tiwe
 *
 */
public class SimpleEvaluator<T> implements Evaluator<T>{
    
    // 16secs
        
    // TODO : make this location configurable
    private static final File dir;
    
    private static final ClassLoader loader;
    
    private static final String classpath;
    
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    
    private static final List<String> compilationOptions;
    
    static{                
        try {
            dir = new File(System.getProperty("java.io.tmpdir"), "files");
            dir.mkdirs();
            
            URLClassLoader parent = (URLClassLoader) SimpleEvaluator.class.getClassLoader();
            classpath = SimpleCompiler.getClassPath(parent);
            loader = new URLClassLoader(new URL[]{dir.toURI().toURL()}, parent);
            
            compilationOptions = Arrays.asList("-classpath",classpath,"-d",dir.getAbsolutePath(),"-g:none");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private final Method method;
    
    private final Object[] constants;
    
    public SimpleEvaluator( 
            String source, 
            Class<? extends T> projectionType, 
            String[] names, 
            Class<?>[] types, 
            Object[] constants) 
            throws IOException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        this.constants = constants;
        
        // create id for evaluator
        String id = toId(source, projectionType, types);
               
        // compile
        File classFile = new File(dir, id+".class");
        if (!classFile.exists()){
            compile(source, projectionType, names, types, id);
        }        
        
        // load class        
        Class<?> cl = loader.loadClass(id);
        method = cl.getMethod("eval", types);
    }

    private void compile(String source, Class<? extends T> projectionType,
            String[] names, Class<?>[] types, String id)
            throws IOException, UnsupportedEncodingException {
        // create source
        StringWriter writer = new StringWriter();
        JavaWriter javaw = new JavaWriter(writer);
        javaw.beginClass(id, null); 
        String[] params = new String[names.length];
        for (int i = 0; i < params.length; i++){
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
                    compilationOptions, null, 
                    Collections.singletonList(javaFileObject));
            if (!task.call().booleanValue()){
                throw new QueryException("Compilation of " + source + " failed.\n" + out.toString());
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
           
    }

    public static String toId(String source, Class<?> returnType, Class<?>... types) {
        StringBuilder b = new StringBuilder("Q");
        b.append("_").append(source.hashCode());
        b.append("_").append(returnType.getName().hashCode());
        for (Class<?> type : types){
            b.append("_").append(type.getName().hashCode());
        }
        return b.toString().replace('-', '0');        
    }
    
    private static String toName(Class<?> cl){
        if (cl.isArray()){
            return toName(cl.getComponentType())+"[]";
        }else if (cl.getPackage() == null || cl.getPackage().getName().equals("java.lang")){
            return cl.getSimpleName();
        }else{
            return cl.getName().replace('$', '.');
        }
    }    

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate(Object... args) {
        try {
            if (constants.length > 0){
                args = EvaluatorFactory.combine(constants.length + args.length, constants, args);    
            }            
            return (T) method.invoke(null, args);
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        } catch (InvocationTargetException e) {
            throw new QueryException(e);
        }
    }

}
