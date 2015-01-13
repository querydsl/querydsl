package com.querydsl.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class JDOExporterMojoTest {

    @Test
    public void Execute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");

        JDOExporterMojo mojo = new JDOExporterMojo();
        mojo.setTargetFolder(new File("target/generated-test-data3"));
        mojo.setPackages(new String[]{"com.querydsl.maven"});
        mojo.setProject( mavenProject);
        mojo.execute();

        File file = new File("target/generated-test-data3/com/mysema/querydsl/maven/QEntity.java");
        assertTrue(file.exists());
    }

}
