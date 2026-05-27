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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Regression test for https://github.com/querydsl/querydsl/issues/3862
 *
 * Verifies that @QueryProjection correctly generates constructors when DTO parameters
 * use Kotlin value classes (@JvmInline).
 */
public class KotlinValueClassProjectionTest extends AbstractProcessorTest {

    private static final String VALUE_CLASS_SOURCE =
            "src/test/java/com/querydsl/apt/domain/KotlinValueClassTest.java";
    private static final String JVMINLINE_SOURCE =
            "src/test/java/kotlin/jvm/JvmInline.java";

    @Test
    public void projection_with_kotlin_value_class_generates_constructor() throws IOException {
        List<String> sources = Arrays.asList(VALUE_CLASS_SOURCE, JVMINLINE_SOURCE);
        process(QuerydslAnnotationProcessor.class, sources, "kotlinValueClassProjection");

        File generatedFile = new File(
                "target/kotlinValueClassProjection/com/querydsl/apt/domain/QKotlinValueClassTest_UserDto.java");
        assertTrue(
                "Q-type for UserDto should be generated when @QueryProjection uses a Kotlin value class",
                generatedFile.exists());

        String generatedSource = new String(java.nio.file.Files.readAllBytes(generatedFile.toPath()));
        assertTrue(
                "Generated Q-type should contain a constructor",
                generatedSource.contains("public QKotlinValueClassTest_UserDto("));
    }
}