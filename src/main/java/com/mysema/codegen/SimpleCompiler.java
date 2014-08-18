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

import javax.lang.model.SourceVersion;
import javax.tools.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.Manifest;

import com.google.common.base.Joiner;

/**
 * SimpleCompiler provides a convenience wrapper of the JavaCompiler interface
 * with automatic classpath generation
 * 
 * @author tiwe
 * 
 */
public class SimpleCompiler implements JavaCompiler {

    private static final Joiner pathJoiner = Joiner.on(File.pathSeparator);

    private static boolean isSureFireBooter(URLClassLoader cl) {
        for (URL url : cl.getURLs()) {
            if (url.getPath().contains("surefirebooter")) {
                return true;
            }
        }

        return false;
    }

    public static String getClassPath(URLClassLoader cl) {
        try {
            List<String> paths = new ArrayList<String>();
            if (isSureFireBooter(cl)) {
                // extract MANIFEST.MF Class-Path entry, since the Java Compiler doesn't handle
                // manifest only jars in the classpath correctly
                URL url = cl.findResource("META-INF/MANIFEST.MF");
                Manifest manifest = new Manifest(url.openStream());
                String classpath = (String) manifest.getMainAttributes().getValue("Class-Path");
                for (String entry : classpath.split(" ")) {
                    URL entryUrl = new URL(entry);
                    String decodedPath = URLDecoder.decode(entryUrl.getPath(), "UTF-8");
                    paths.add(new File(decodedPath).getAbsolutePath());
                }
            } else {
                ClassLoader c = cl;
                while (c instanceof URLClassLoader) {
                    for (URL url : ((URLClassLoader)c).getURLs()) {
                        String decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
                        paths.add(new File(decodedPath).getAbsolutePath());
                    }
                    c = c.getParent();
                }
            }            
            return pathJoiner.join(paths);
        } catch (UnsupportedEncodingException e) {
            throw new CodegenException(e);
        } catch (IOException e) {
            throw new CodegenException(e);
        }
    }

    private final ClassLoader classLoader;

    private String classPath;

    private final JavaCompiler compiler;

    public SimpleCompiler() {
        this(ToolProvider.getSystemJavaCompiler(), Thread.currentThread().getContextClassLoader());
    }

    public SimpleCompiler(JavaCompiler compiler, ClassLoader classLoader) {
        this.compiler = compiler;
        this.classLoader = classLoader;
    }

    private String getClasspath() {
        if (classPath == null) {
            if (classLoader instanceof URLClassLoader) {
                classPath = getClassPath((URLClassLoader) classLoader);
            } else {
                throw new IllegalArgumentException("Unsupported ClassLoader " + classLoader);
            }
        }
        return classPath;
    }

    @Override
    public Set<SourceVersion> getSourceVersions() {
        return compiler.getSourceVersions();
    }

    @Override
    public StandardJavaFileManager getStandardFileManager(
            DiagnosticListener<? super JavaFileObject> diagnosticListener, Locale locale,
            Charset charset) {
        return compiler.getStandardFileManager(diagnosticListener, locale, charset);
    }

    @Override
    public CompilationTask getTask(Writer out, JavaFileManager fileManager,
            DiagnosticListener<? super JavaFileObject> diagnosticListener,
            Iterable<String> options, Iterable<String> classes,
            Iterable<? extends JavaFileObject> compilationUnits) {
        return compiler.getTask(out, fileManager, diagnosticListener, options, classes,
                compilationUnits);
    }

    @Override
    public int isSupportedOption(String option) {
        return compiler.isSupportedOption(option);
    }

    @Override
    public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
        for (String a : arguments) {
            if (a.equals("-classpath")) {
                return compiler.run(in, out, err, arguments);
            }
        }

        // no classpath given
        List<String> args = new ArrayList<String>(arguments.length + 2);
        args.add("-classpath");
        args.add(getClasspath());
        for (String arg : arguments) {
            args.add(arg);
        }
        return compiler.run(in, out, err, args.toArray(new String[args.size()]));
    }

}
