package com.mysema.query.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.mysema.query.sql.types.BytesType;
import com.mysema.query.sql.types.DateTimeType;
import com.mysema.query.sql.types.LocalDateType;
import com.mysema.query.sql.types.LocalTimeType;

public class MetadataExportMojoTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();

    private final MavenProject project = new MavenProject();
    
    private final MetadataExportMojo mojo = new MetadataExportMojo();
    
    @Test
    public void Execute() throws SecurityException, NoSuchFieldException, IllegalAccessException, MojoExecutionException, MojoFailureException {
        set(mojo, "project", project);
        set(mojo, "jdbcDriver", "org.h2.Driver");
        set(mojo, "jdbcUrl", url);
        set(mojo, "jdbcUser", "sa");
        set(mojo, "namePrefix", "Q"); // default value
        set(mojo, "packageName", "com.example");
        set(mojo, "targetFolder", "target/export");

        mojo.execute();

        assertEquals(Collections.singletonList("target/export"), project.getCompileSourceRoots());
        assertTrue(new File("target/export").exists());

    }

    @Test
    public void Execute_With_CustomTypes() throws SecurityException, NoSuchFieldException, IllegalAccessException, MojoExecutionException, MojoFailureException {
        set(mojo, "project", project);
        set(mojo, "jdbcDriver", "org.h2.Driver");
        set(mojo, "jdbcUrl", url);
        set(mojo, "jdbcUser", "sa");
        set(mojo, "namePrefix", "Q"); // default value
        set(mojo, "packageName", "com.example");
        set(mojo, "targetFolder", "target/export2");
        set(mojo, "customTypes", new String[]{BytesType.class.getName()});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export2"), project.getCompileSourceRoots());
        assertTrue(new File("target/export2").exists());
    }
    
    @Test
    public void Execute_With_JodaTypes() throws MojoExecutionException, MojoFailureException, SecurityException, NoSuchFieldException, IllegalAccessException{
        set(mojo, "project", project);
        set(mojo, "jdbcDriver", "org.h2.Driver");
        set(mojo, "jdbcUrl", url);
        set(mojo, "jdbcUser", "sa");
        set(mojo, "namePrefix", "Q"); // default value
        set(mojo, "packageName", "com.example");
        set(mojo, "targetFolder", "target/export3");
        set(mojo, "customTypes", new String[]{LocalDateType.class.getName(), LocalTimeType.class.getName(), DateTimeType.class.getName()});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export3"), project.getCompileSourceRoots());
        assertTrue(new File("target/export3").exists());
    }

    private void set(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalAccessException{
        Field field = AbstractMetaDataExportMojo.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

}
