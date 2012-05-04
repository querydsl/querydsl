/*
 * Copyright 2011, Mysema Ltd
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

import com.mysema.query.sql.types.BytesType;
import com.mysema.query.sql.types.DateTimeType;
import com.mysema.query.sql.types.LocalDateType;
import com.mysema.query.sql.types.LocalTimeType;

public class MetadataExportMojoTest extends AbstractMojoTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();

    private final MavenProject project = new MavenProject();
    
    private final MetadataExportMojo mojo = new MetadataExportMojo();

    public MetadataExportMojoTest() {
        super(AbstractMetaDataExportMojo.class);
    }
    
    @Test
    public void Execute() throws Exception {
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
    public void Execute_With_CustomTypes() throws Exception {
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
    public void Execute_With_JodaTypes() throws Exception{
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

}
