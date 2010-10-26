package com.mysema.query.apt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;

import com.mysema.codegen.SimpleCompiler;

public abstract class AbstractProcessorTest {

    protected List<String> getFiles(String path) {
        List<String> classes = new ArrayList<String>();
        for (File file : new File(path).listFiles()){
            if (file.getName().endsWith(".java")){
                classes.add(file.getPath());
            }else if (file.isDirectory() && !file.getName().startsWith(".")){
                classes.addAll(getFiles(file.getAbsolutePath()));
            }
        }
        return classes;
    }

    protected void process(Class<? extends AbstractProcessor> processorClass, List<String> classes, String target) throws IOException{
        File out = new File("target/" + target);
        FileUtils.deleteDirectory(out);
        if (!out.mkdirs()){
            Assert.fail("Creation of " + out.getPath() + " failed");
        }
        compile(processorClass, classes, target);
    } 
    
    protected void compile(Class<? extends AbstractProcessor> processorClass, List<String> classes, String target) throws IOException{
        JavaCompiler compiler = new SimpleCompiler();
        System.out.println(compiler.getClass().getName());
        List<String> options = new ArrayList<String>(classes.size() + 3);
        options.add("-s");
        options.add("target/" + target);
        options.add("-proc:only");
        options.add("-processor");
        options.add(processorClass.getName());
        options.add("-sourcepath");
        options.add("src/test/java");
        options.addAll(getAPTOptions());
        options.addAll(classes);
        int compilationResult = compiler.run(null, null, null, options.toArray(new String[options.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }

    protected Collection<String> getAPTOptions() {
        return Collections.emptyList();
    }

}
