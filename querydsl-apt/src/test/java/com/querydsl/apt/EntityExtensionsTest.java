/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.apt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EntityExtensionsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/querydsl/apt/";

    @Test
    public void handles_entity_extensions_correctly() throws IOException, InterruptedException {
        File source = new File(packagePath,  "EntityWithExtensions.java");
        File source2 = new File(packagePath, "EntityExtensions.java");
        List<String> sources = Arrays.asList(source.getPath(), source2.getPath());
        File qType = new File("target/overwrite2/com/querydsl/apt/QEntityWithExtensions.java");

        // QEntityWithExtensions is generated
        process(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue(qType.exists());
        long modified = qType.lastModified();
        Thread.sleep(1000);
        assertTrue(new String(Files.readAllBytes(qType.toPath()), StandardCharsets.UTF_8).contains("extension()"));

        // EntityWithExtensions has not changed, QEntityWithExtensions is not overwritten
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertEquals(modified, qType.lastModified());

        // EntityWithExtensions is updated, QEntityWithExtensions is overwritten
        Files.createFile(source.toPath());
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue("" + modified + " >= " + qType.lastModified(), modified < qType.lastModified());
        assertTrue(new String(Files.readAllBytes(qType.toPath()), StandardCharsets.UTF_8).contains("extension()"));

        // QEntityWithExtensions is deleted and regenerated
        assertTrue(qType.delete());
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue(qType.exists());
        assertTrue(new String(Files.readAllBytes(qType.toPath()), StandardCharsets.UTF_8).contains("extension()"));
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Collections.singletonList("-AdefaultOverwrite=true");
    }

}
