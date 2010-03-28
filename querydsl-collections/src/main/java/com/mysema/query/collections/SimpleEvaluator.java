package com.mysema.query.collections;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

import com.mysema.query.QueryException;
import com.mysema.util.JavaWriter;
import com.mysema.util.SimpleCompiler;

/**
 * @author tiwe
 *
 */
public class SimpleEvaluator<T> implements Evaluator<T>{
        
    private static final File dir;
    
    private static final ClassLoader loader;
    
    private static final String classpath;
    
    private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    
    static{                
        try {
            dir = new File(System.getProperty("java.io.tmpdir"), "files");
            dir.mkdirs();
            
            URLClassLoader parent = (URLClassLoader) SimpleEvaluator.class.getClassLoader();
            classpath = SimpleCompiler.getClassPath(parent);
            loader = new URLClassLoader(new URL[]{dir.toURI().toURL()}, parent);
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
        File javaFile = new File(dir, id+".java");
        if (!new File(dir, id+".class").exists()){     
//            long start = System.currentTimeMillis();
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
            
            FileUtils.writeByteArrayToFile(javaFile, writer.toString().getBytes("ISO-8859-1"));
            compiler.run(null, null, null, "-classpath", classpath, javaFile.getAbsolutePath());
            // source file is not needed anymore
            javaFile.delete();
//            long duration = System.currentTimeMillis() - start;
//            System.out.println("- " + duration + ": " + source);
        }        
        
        // load class        
        Class<?> cl = loader.loadClass(id);
        method = cl.getMethod("eval", types);
    }

    private static String toId(String source, Class<?> projectionType, Class<?>[] types) {
        StringBuilder b = new StringBuilder("Q");
        b.append("_").append(Math.abs(source.hashCode()));
        b.append("_").append(Math.abs(projectionType.hashCode()));
        for (Class<?> type : types){
            b.append("_").append(Math.abs(type.hashCode()));
        }
        return b.toString();        
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
