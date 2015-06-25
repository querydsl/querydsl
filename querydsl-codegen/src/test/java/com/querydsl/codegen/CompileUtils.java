package com.querydsl.codegen;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;

import com.mysema.codegen.MemFileManager;
import com.mysema.codegen.MemSourceFileObject;
import com.mysema.codegen.SimpleCompiler;

import junit.framework.Assert;

public final class CompileUtils {

    private CompileUtils() { }

    public static void assertCompiles(String name, String source) {
        URLClassLoader parent = (URLClassLoader) CompileUtils.class.getClassLoader();
        SimpleCompiler compiler = new SimpleCompiler();
        MemFileManager fileManager = new MemFileManager(parent, compiler.getStandardFileManager(null, null, null));
        String classpath = SimpleCompiler.getClassPath(parent);
        List<String> compilationOptions = Arrays.asList("-classpath", classpath, "-g:none");

        // compile
        SimpleJavaFileObject javaFileObject = new MemSourceFileObject(name, source);
        Writer out = new StringWriter();
        JavaCompiler.CompilationTask task = compiler.getTask(out, fileManager, null, compilationOptions, null,
                Collections.singletonList(javaFileObject));
        if (!task.call()) {
            Assert.fail("Compilation of " + source + " failed.\n" + out.toString());
        }

    }

}
