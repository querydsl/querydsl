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

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ServiceLoader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Morphia annotation processor service registration
 */
public class MorphiaAnnotationProcessorTest {

    @Test
    public void javaxAnnotationProcessorServiceFileExists() throws Exception {
        String serviceFile = "META-INF/services/javax.annotation.processing.Processor";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(serviceFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            assertThat(is).isNotNull();
            String line = reader.readLine();
            assertThat(line).isEqualTo("com.querydsl.apt.morphia.MorphiaAnnotationProcessor");
        }
    }

    @Test
    public void jakartaAnnotationProcessorServiceFileExists() throws Exception {
        String serviceFile = "META-INF/services/jakarta.annotation.processing.Processor";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(serviceFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            assertThat(is).isNotNull();
            String line = reader.readLine();
            assertThat(line).isEqualTo("com.querydsl.apt.morphia.MorphiaAnnotationProcessor");
        }
    }
}
