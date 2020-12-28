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
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.Type;

/**
 * JDKEvaluatorFactory is a factory implementation for creating Evaluator instances
 * 
 * @author tiwe
 * 
 */
public class JDKEvaluatorFactory extends AbstractEvaluatorFactory {

    private final MemFileManager fileManager;

    private final String classpath;

    private final List<String> compilationOptions;

    private final JavaCompiler compiler;

    public JDKEvaluatorFactory(URLClassLoader parent) {
        this(parent, ToolProvider.getSystemJavaCompiler());
    }

    public JDKEvaluatorFactory(URLClassLoader parent, JavaCompiler compiler) {
        this.fileManager = new MemFileManager(parent, compiler.getStandardFileManager(null, null, null));
        this.compiler = compiler;
        this.classpath = SimpleCompiler.getClassPath(parent);
        this.loader = fileManager.getClassLoader(StandardLocation.CLASS_OUTPUT);
        this.compilationOptions = Arrays.asList("-classpath", classpath, "-g:none");
    }

    protected void compile(String source, ClassType projectionType, String[] names, Type[] types,
                           String id, Map<String, Object> constants) throws IOException {
        // create source
        source = createSource(source, projectionType, names, types, id, constants);

        // compile
        SimpleJavaFileObject javaFileObject = new MemSourceFileObject(id, source);
        Writer out = new StringWriter();

        CompilationTask task = compiler.getTask(out, fileManager, null, compilationOptions, null,
                Collections.singletonList(javaFileObject));
        if (!task.call().booleanValue()) {
            throw new CodegenException("Compilation of " + source + " failed.\n" + out.toString());
        }
    }

}
