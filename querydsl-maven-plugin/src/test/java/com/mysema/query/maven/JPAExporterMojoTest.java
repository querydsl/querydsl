package com.mysema.query.maven;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class JPAExporterMojoTest {

    @Test
    public void Execute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");

        JPAExporterMojo mojo = new JPAExporterMojo();
        mojo.setTargetFolder(new File("target/generated-test-data2"));
        mojo.setPackages(new String[]{"com.mysema.query.maven"});
        mojo.setProject( mavenProject);
        mojo.execute();

        File file = new File("target/generated-test-data2/com/mysema/query/maven/QEntity.java");
        assertTrue(file.exists());
    }

}
