/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;

import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mysema.codegen.SimpleCompiler;
import com.querydsl.core.util.FileUtils;

public class EclipseCompilationTest {

    private static final String packagePath = "src/test/apt/com/mysema/querydsl/eclipse/";

    @Test
    @Ignore
    public void test() throws IOException {
        System.setProperty("jdt.compiler.useSingleThread", "true");
        // select classes
        List<String> classes = new ArrayList<String>();
        for (File file : new File(packagePath).listFiles()) {
            if (file.getName().endsWith(".java")) {
                classes.add(file.getPath());
            }
        }

        // prepare output
        File out = new File("target/out-eclipse");
        FileUtils.delete(out);
        if (!out.mkdirs()) {
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
        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            Assert.fail("Compilation Failed");
        }

        File resultFile = new File("target/out-eclipse/com/mysema/querydsl/eclipse/QSimpleEntity.java");
        assertTrue(resultFile.exists());
        String result = Files.toString(resultFile, Charsets.UTF_8);
        assertTrue(result.contains("NumberPath<java.math.BigDecimal> bigDecimalProp"));
        assertTrue(result.contains("NumberPath<Integer> integerProp"));
        assertTrue(result.contains("NumberPath<Integer> intProp"));
        assertTrue(result.contains("StringPath stringProp"));
    }

}
