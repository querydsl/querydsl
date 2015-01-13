/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.querydsl.codegen.GenericExporter;
import com.querydsl.codegen.Serializer;
import com.querydsl.codegen.TypeMappings;

/**
 * AbstractExporterMojo calls the {@link GenericExporter} tool using the
 * classpath of the module the plugin is invoked in.
 *
 */
public abstract class AbstractExporterMojo extends AbstractMojo {

    /**
     * @parameter required=true
     */
    private File targetFolder;

    /**
     * @parameter default-value=false
     */
    private boolean scala;

    /**
     * @parameter required=true
     */
    private String[] packages;

    /**
     * @parameter default-value=true
     */
    private boolean handleFields = true;

    /**
     * @parameter default-value=true
     */
    private boolean handleMethods = true;

    /**
     * @parameter expression="${project}" readonly=true required=true
     */
    private MavenProject project;

    /**
     * @parameter
     */
    private String sourceEncoding;

    /**
     * @parameter default-value=false
     */
    private boolean testClasspath;

    /**
     * @component
     */
    private BuildContext buildContext;

    @SuppressWarnings("unchecked")
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (testClasspath) {
            project.addTestCompileSourceRoot(targetFolder.getAbsolutePath());
        } else {
            project.addCompileSourceRoot(targetFolder.getAbsolutePath());
        }
        if (!hasSourceChanges()) {
            // Only run if something has changed on the source dirs. This will
            // avoid m2e entering on a infinite build.
            return;
        }

        ClassLoader classLoader = null;
        try {
            classLoader = getProjectClassLoader();
        } catch (MalformedURLException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }

        Charset charset = sourceEncoding != null ? Charset.forName(sourceEncoding) : Charset.defaultCharset();
        GenericExporter exporter = new GenericExporter(classLoader, charset);
        exporter.setTargetFolder(targetFolder);

        if (scala) {
            try {
                exporter.setSerializerClass((Class<? extends Serializer>) Class
                        .forName("com.querydsl.scala.ScalaEntitySerializer"));
                exporter.setTypeMappingsClass((Class<? extends TypeMappings>) Class
                        .forName("com.querydsl.scala.ScalaTypeMappings"));
                exporter.setCreateScalaSources(true);
            } catch (ClassNotFoundException e) {
                throw new MojoFailureException(e.getMessage(), e);
            }
        }

        configure(exporter);
        exporter.export(packages);
    }

    /**
     * Configures the {@link GenericExporter}; subclasses may override if desired.
     */
    protected void configure(GenericExporter exporter) {
        exporter.setHandleFields(handleFields);
        exporter.setHandleMethods(handleMethods);
    }

    @SuppressWarnings("unchecked")
    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException,
            MalformedURLException {
        List<String> classpathElements;
        if (testClasspath) {
            classpathElements = project.getTestClasspathElements();
        } else {
            classpathElements = project.getCompileClasspathElements();
        }
        List<URL> urls = new ArrayList<URL>(classpathElements.size());
        for (String element : classpathElements) {
            File file = new File(element);
            if (file.exists()) {
                urls.add(file.toURI().toURL());
            }
        }
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
    }

    @SuppressWarnings("rawtypes")
    private boolean hasSourceChanges() {
        if (buildContext != null) {
            List sourceRoots = testClasspath ? project.getTestCompileSourceRoots() :
                                               project.getCompileSourceRoots();
            for (Object path : sourceRoots) {
                if (buildContext.hasDelta(new File(path.toString()))) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setScala(boolean scala) {
        this.scala = scala;
    }

    public void setPackages(String[] packages) {
        this.packages = packages;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

    public void setTestClasspath(boolean testClasspath) {
        this.testClasspath = testClasspath;
    }

    public void setBuildContext(BuildContext buildContext) {
        this.buildContext = buildContext;
    }

    public void setHandleFields(boolean handleFields) {
        this.handleFields = handleFields;
    }

    public void setHandleMethods(boolean handleMethods) {
        this.handleMethods = handleMethods;
    }
}
