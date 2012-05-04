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


public class TestMetadataExportMojoTest extends AbstractMojoTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();
    
    public TestMetadataExportMojoTest() {
        super(AbstractMetaDataExportMojo.class);
    }
    
    @Test
    public void Execute() throws Exception {
        MavenProject project = new MavenProject();
        TestMetadataExportMojo mojo = new TestMetadataExportMojo();
        set(mojo, "project", project);
        set(mojo, "jdbcDriver", "org.h2.Driver");
        set(mojo, "jdbcUrl", url);
        set(mojo, "jdbcUser", "sa");
        set(mojo, "namePrefix", "Q"); // default value
        set(mojo, "nameSuffix", "");
        set(mojo, "beanPrefix", "");
        set(mojo, "beanSuffix", "Bean");
        set(mojo, "packageName", "com.example");
        set(mojo, "targetFolder", "target/export2");

        mojo.execute();

        assertEquals(Collections.singletonList("target/export2"), project.getTestCompileSourceRoots());
        assertTrue(new File("target/export2").exists());
    }
    
}
