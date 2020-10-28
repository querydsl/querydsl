package com.querydsl.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;

import com.querydsl.codegen.GeneratedAnnotationResolver;

public class GenericExporterMojoTest {

    public static final File Q_ENTITY_SOURCE_FILE = new File("target/generated-test-data/com/querydsl/maven/QEntity.java");

    private GenericExporterMojo prepareMojo() {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");

        GenericExporterMojo mojo = new GenericExporterMojo();
        mojo.setTargetFolder(new File("target/generated-test-data"));
        mojo.setPackages(new String[] {"com.querydsl.maven"});
        mojo.setProject(mavenProject);
        return mojo;
    }

    @Test
    public void execute() throws Exception {
        GenericExporterMojo mojo = prepareMojo();
        mojo.execute();

        assertTrue(Q_ENTITY_SOURCE_FILE.exists());
    }

    @Test
    public void defaultGeneratedAnnotation() throws Exception {
        GenericExporterMojo mojo = prepareMojo();
        mojo.execute();

        File file = Q_ENTITY_SOURCE_FILE;
        String source = FileUtils.fileRead(file);
        assertTrue(source.contains("@" + GeneratedAnnotationResolver.resolveDefault()));
    }

    @Test
    public void providedGeneratedAnnotation() throws Exception {
        String annotationClass = com.querydsl.core.annotations.Generated.class.getName();
        GenericExporterMojo mojo = prepareMojo();
        mojo.setGeneratedAnnotationClass(annotationClass);
        mojo.execute();

        File file = Q_ENTITY_SOURCE_FILE;
        String source = FileUtils.fileRead(file);
        assertTrue(source.contains("@" + annotationClass));
    }

}
