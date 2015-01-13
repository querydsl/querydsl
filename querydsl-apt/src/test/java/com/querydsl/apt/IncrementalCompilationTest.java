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
package com.querydsl.apt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.Files;

@Ignore
public class IncrementalCompilationTest extends AbstractProcessorTest{
    
    private static final String packagePath = "src/test/java/com/mysema/querydsl/domain/";

    @Test
    public void Does_Not_Overwrite_Unchanged_Files() throws IOException, InterruptedException {
        File source = new File(packagePath, "ExampleEntity.java");
        String path = source.getPath();
        File qType = new File("target/overwrite/com/mysema/querydsl/domain/QExampleEntity.java");
        
        // QTestEntity is generated
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue(qType.exists());
        long modified = qType.lastModified();
        Thread.sleep(1000);
        
        // TestEntity has not changed, QTestEntity is not overwritten
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertEquals(modified, qType.lastModified());

        // TestEntity is updated, QTestEntity is overwritten
        Files.touch(source);
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue("" + modified + " >= " + qType.lastModified(), modified < qType.lastModified());
        
        // QTestEntity is deleted and regenerated 
        assertTrue(qType.delete());
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue(qType.exists());
    }
    
}
