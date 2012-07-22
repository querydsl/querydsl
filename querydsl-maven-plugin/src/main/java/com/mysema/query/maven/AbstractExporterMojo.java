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
package com.mysema.query.maven;

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

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.TypeMappings;

/**
 * AbstractExporterMojo calls the {@link GenericExporter} tool using the classpath of the module
 * the plugin is invoked in.
 * 
 */
public abstract class AbstractExporterMojo extends AbstractMojo {

    /**
     * @parameter
     */
    private File targetFolder;
    
    /**
     * @parameter default-value=false
     */
    private boolean scala;
        
    /**
     * @parameter
     */
    private String[] packages;
    
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
        
    @SuppressWarnings("unchecked")
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
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
                exporter.setSerializerClass((Class<? extends Serializer>) 
                        Class.forName("com.mysema.query.scala.ScalaEntitySerializer"));
                exporter.setTypeMappingsClass((Class<? extends TypeMappings>) 
                        Class.forName("com.mysema.query.scala.ScalaTypeMappings"));
                exporter.setCreateScalaSources(true);
            } catch (ClassNotFoundException e) {
                throw new MojoFailureException(e.getMessage(), e);
            }            
        }
        
        configure(exporter);
        
        exporter.export(packages);
    }
    
    protected abstract void configure(GenericExporter exporter);
    
   
    @SuppressWarnings("unchecked")
    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException, MalformedURLException {
        List<String> classpathElements;
        if (testClasspath) {
            classpathElements = project.getTestClasspathElements();
        } else {
            classpathElements = project.getCompileClasspathElements();
        }
        List<URL> urls = new ArrayList<URL>(classpathElements.size());
        for (String element : classpathElements){
            File file = new File(element);
            if (file.exists()){
                urls.add(file.toURI().toURL());
            }
        }
        return new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
    }

}
