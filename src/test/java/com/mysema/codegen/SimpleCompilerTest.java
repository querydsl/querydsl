/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

public class SimpleCompilerTest {
    
    @After
    public void tearDown(){
        new File("src/test/java/com/mysema/codegen/SimpleCompilerTest.class").delete();
    }

    @Test
    public void testRun() {
        new File("target/out").mkdir();
        JavaCompiler compiler = new SimpleCompiler();
        System.out.println(compiler.getClass().getName());
        List<String> options = new ArrayList<String>(3);
        options.add("-s");
        options.add("target/out");
        options.add("src/test/java/com/mysema/codegen/SimpleCompilerTest.java");
        int compilationResult = compiler.run(null, null, null, options.toArray(new String[options.size()]));
        if(compilationResult != 0){
            Assert.fail("Compilation Failed");
        }
    }

}
