package com.mysema.query.maven;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

public class GenericExporterMojoTest extends AbstractMojoTest {

    public GenericExporterMojoTest() {
        super(AbstractExporterMojo.class);
    }

    @Test
    public void Execute() throws Exception {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/classes");
        
        GenericExporterMojo mojo = new GenericExporterMojo();
        set(mojo, "targetFolder", "target/generated-test-data");
        set(mojo, "packages", new String[]{"com.mysema.query.maven"});
        set(mojo, "project", mavenProject);
        mojo.execute();
        
        File file = new File("target/generated-test-data/com/mysema/query/maven/QEntity.java"); 
        assertTrue(file.exists());
    }

}
