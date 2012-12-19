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
package com.mysema.codegen;

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

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.support.ClassUtils;

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
        StringWriter writer = new StringWriter();
        JavaWriter javaw = new JavaWriter(writer);
        SimpleType idType = new SimpleType(id, "", id);
        javaw.beginClass(idType, null);
        Parameter[] params = new Parameter[names.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = new Parameter(names[i], types[i]);
        }

        for (Map.Entry<String, Object> entry : constants.entrySet()) {
            Type type = new ClassType(TypeCategory.SIMPLE, ClassUtils.normalize(entry.getValue().getClass()));
            javaw.publicField(type, entry.getKey());
        }

        if (constants.isEmpty()) {
            javaw.beginStaticMethod(projectionType, "eval", params);
        } else {
            javaw.beginPublicMethod(projectionType, "eval", params);
        }
        javaw.append(source);
        javaw.end();
        javaw.end();

        // compile
        SimpleJavaFileObject javaFileObject = new MemSourceFileObject(id, writer.toString());
        Writer out = new StringWriter();

        CompilationTask task = compiler.getTask(out, fileManager, null, compilationOptions, null,
                Collections.singletonList(javaFileObject));
        if (!task.call().booleanValue()) {
            throw new CodegenException("Compilation of " + source + " failed.\n" + out.toString());
        }

    }

}
