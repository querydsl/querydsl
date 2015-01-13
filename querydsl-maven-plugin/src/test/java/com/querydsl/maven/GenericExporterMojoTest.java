package com.querydsl.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class GenericExporterMojoTest {

    @Test
    public void Execute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");
        
        GenericExporterMojo mojo = new GenericExporterMojo();
        mojo.setTargetFolder(new File("target/generated-test-data"));
        mojo.setPackages(new String[]{"com.querydsl.maven"});
        mojo.setProject( mavenProject);
        mojo.execute();
        
        File file = new File("target/generated-test-data/com/mysema/querydsl/maven/QEntity.java");
        assertTrue(file.exists());
    }
    
}
