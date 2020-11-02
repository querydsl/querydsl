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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BooleanExtensionsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/apt/com/querydsl/";

    @Test
    public void process() throws IOException {
        List<String> sources = Arrays.asList(
                new File(packagePath, "BooleanExtensions.java").getPath(),
                new File(packagePath, "ExampleEntity.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources,"booleanExtensions");
        String qtypeContent = new String(Files.readAllBytes(new File("target/booleanExtensions/com/querydsl/QExampleEntity.java").toPath()), StandardCharsets.UTF_8);
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp2"));
    }

    @Test
    public void process2() throws IOException {
        List<String> sources = Arrays.asList(
                new File(packagePath, "BooleanExtensions2.java").getPath(),
                new File(packagePath, "ExampleEntity.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources,"booleanExtensions2");
        String qtypeContent = new String(Files.readAllBytes(new File("target/booleanExtensions2/com/querydsl/QExampleEntity.java").toPath()), StandardCharsets.UTF_8);
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp2"));
    }

}
