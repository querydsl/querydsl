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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class UnknownAsEmbeddableTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/mysema/querydsl/domain";

    @Test
    public void Process() throws IOException {
        List<String> classes = getFiles(packagePath);
        process(QuerydslAnnotationProcessor.class, classes,"unknownAsEmbeddable");

        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/querydsl/domain/custom/QEntity.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/querydsl/domain/custom/QEmbeddedType.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/querydsl/domain/custom/QEmbeddedType2.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/querydsl/domain/custom/QEmbeddedType3.java").exists());
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.unknownAsEmbeddable=true");
    }
    
}
