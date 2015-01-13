package com.querydsl.maven;

import java.io.File;

import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.sonatype.plexus.build.incremental.DefaultBuildContext;

public class CompileMojoTest {

    @Test
    public void test() throws Exception {
        new File("target/compile-mojo").mkdir();
        MavenProject mavenProject = new MavenProject();
        mavenProject.getBuild().setOutputDirectory("target/compile-mojo");

        CompileMojo mojo = new CompileMojo();
        mojo.setBuildContext(new DefaultBuildContext());
        mojo.setProject(mavenProject);
        mojo.setSourceFolder(new File("src/test/java"));
        mojo.execute();
    }

}
