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

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.TypeMappings;

/**
 * GenericExporterMojo calls the GenericExporter tool using the classpath of the module
 * 
 * @goal generic-export
 * @requiresDependencyResolution compile
 */
public class GenericExporterMojo extends AbstractMojo {

    /**
     * @parameter
     */
    private String targetFolder;
    
    /**
     * @parameter default-value=false
     */
    private boolean scala;
    
    /**
     * @parameter default-value=false
     */
    private boolean jpa;
    
    /**
     * @parameter default-value=false
     */
    private boolean jdo;
    
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
        
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // TODO : create classLoader for project compile classpath
        ClassLoader classLoader = null;
        Charset charset = sourceEncoding != null ? Charset.forName(sourceEncoding) : Charset.defaultCharset();
        try {
            classLoader = getProjectClassLoader();
        } catch (MalformedURLException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
        
        GenericExporter exporter = new GenericExporter(classLoader, charset);
        exporter.setTargetFolder(new java.io.File(targetFolder));
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
        
        if (jpa) {
            exporter.setEmbeddableAnnotation(Embeddable.class);
            exporter.setEmbeddedAnnotation(Embedded.class);
            exporter.setEntityAnnotation(Entity.class);
            exporter.setSkipAnnotation(Transient.class);
            exporter.setSupertypeAnnotation(MappedSuperclass.class);    
        } else if (jdo) {
            exporter.setEmbeddableAnnotation(EmbeddedOnly.class);
            exporter.setEmbeddedAnnotation(Embedded.class);
            exporter.setEntityAnnotation(PersistenceCapable.class);
            exporter.setSkipAnnotation(NotPersistent.class);
            exporter.setSupertypeAnnotation(null);
        }        
        exporter.export(packages);
    }
    
    protected ClassLoader getProjectClassLoader() throws DependencyResolutionRequiredException, MalformedURLException {
        List<String> classpathElements = project.getCompileClasspathElements();
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
