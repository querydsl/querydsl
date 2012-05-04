package com.mysema.query.maven;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.mysema.query.codegen.GenericExporter;

/**
 * 
 * @goal generic-export
 *
 */
public class GenericExporterMojo extends AbstractMojo {

    /**
     * @parameter
     */
    private String targetFolder;
    
    /**
     * @parameter
     */
    private boolean scala;
    
    /**
     * @parameter
     */
    private boolean jpa;
    
    /**
     * @parameter
     */
    private boolean jdo;
    
    /**
     * @parameter
     */
    private String[] packages;
        
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new java.io.File(targetFolder));
        if (scala) {
//            exporter.setSerializerClass(ScalaEntitySerializer.class);
//            exporter.setTypeMappingsClass(ScalaTypeMappings.class);
            exporter.setCreateScalaSources(true);
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

}
