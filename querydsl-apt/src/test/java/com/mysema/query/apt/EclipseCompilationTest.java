package com.mysema.query.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.codegen.SimpleCompiler;

public class EclipseCompilationTest {
    
    private static final String packagePath = "src/test/apt/com/mysema/query/eclipse/";
    
    @Test
//    @Ignore
    public void test() throws IOException{
        System.setProperty("jdt.compiler.useSingleThread", "true");
        // select classes
        List<String> classes = new ArrayList<String>();
        for (File file : new File(packagePath).listFiles()){
            if (file.getName().endsWith(".java")){
                classes.add(file.getPath());
            }
        }
        
        // prepare output
        File out = new File("target/out-eclipse");
        FileUtils.deleteDirectory(out);
        if (!out.mkdirs()){
            Assert.fail("Creation of " + out.getPath() + " failed");
        }
        
        String classPath = SimpleCompiler.getClassPath((URLClassLoader) getClass().getClassLoader());
        JavaCompiler compiler = new EclipseCompiler();
        List<String> options = new ArrayList<String>();
        options.add("-s");
        options.add("target/out-eclipse");
        options.add("-proc:only");
        options.add("-processor");
        options.add(QuerydslAnnotationProcessor.class.getName());
        options.add("-Aquerydsl.entityAccessors=true");        
        options.add("-cp");
        options.add(classPath);
        options.add("-source");
        options.add("1.6");
        options.add("-verbose");
        options.addAll(classes);        
        
        int compilationResult = compiler.run(null, System.out, System.err, options.toArray(new String[options.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
        
        File resultFile = new File("target/out-eclipse/com/mysema/query/eclipse/QSimpleEntity.java");
        assertTrue(resultFile.exists());
        String result = FileUtils.readFileToString(resultFile);
        assertTrue(result.contains("PNumber<java.math.BigDecimal> bigDecimalProp"));
        assertTrue(result.contains("PNumber<Integer> integerProp"));
        assertTrue(result.contains("PNumber<Integer> intProp"));
        assertTrue(result.contains("PString stringProp"));
    }

}
