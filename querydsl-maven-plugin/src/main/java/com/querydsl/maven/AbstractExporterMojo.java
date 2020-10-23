/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import javax.annotation.Nullable;

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
 * {@code AbstractExporterMojo} calls {@link GenericExporter} using the
 * classpath of the module in which the plugin is invoked.
 *
 */
public abstract class AbstractExporterMojo extends AbstractMojo {

    /**
     * target folder for sources
     *
     * @parameter
     * @required
     */
    private File targetFolder;

    /**
     * switch for scala source generation
     *
     * @parameter default-value=false
     */
    private boolean scala;

    /**
     * packages to be exported
     *
     * @parameter
     * @required
     */
    private String[] packages;

    /**
     * switch for inspecting fields
     *
     * @parameter default-value=true
     */
    private boolean handleFields = true;

    /**
     * switch for inspecting getters
     *
     * @parameter default-value=true
     */
    private boolean handleMethods = true;

    /**
     * switch for usage of field types instead of getter types
     *
     * @parameter default-value=false
     */
    private boolean useFieldTypes = false;

    /**
     * maven project
     *
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * source file encoding
     *
     * @parameter
     */
    private String sourceEncoding;

    /**
     * test classpath usage switch
     *
     * @parameter default-value=false
     */
    private boolean testClasspath;

    /**
     * Whether to skip the exporting execution
     *
     * @parameter default-value=false property="maven.querydsl.skip"
     */
    private boolean skip;

    /**
     * The fully qualified class name of the <em>Single-Element Annotation</em> (with <code>String</code> element) to put on the generated sources. Defaults to
     * <code>javax.annotation.Generated</code> or <code>javax.annotation.processing.Generated</code> depending on the java version.
     * <em>See also</em> <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     *
     * @parameter
     */
    private String generatedAnnotationClass;

    /**
     * build context
     *
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
        if (skip || !hasSourceChanges()) {
            // Only run if something has changed in the source directories. This will
            // prevent m2e from entering an infinite build cycle.
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
        exporter.setUseFieldTypes(useFieldTypes);
        exporter.setGeneratedAnnotationClass(generatedAnnotationClass);
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

    public void setSkip(boolean skip) {
        this.skip = skip;
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

    public void setUseFieldTypes(boolean useFieldTypes) {
        this.useFieldTypes = useFieldTypes;
    }

    public void setGeneratedAnnotationClass(@Nullable String generatedAnnotationClass) {
        this.generatedAnnotationClass = generatedAnnotationClass;
    }
}
