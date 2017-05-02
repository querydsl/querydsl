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

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class NamesAsConstantsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/querydsl/apt/domain/";

    @Test
    public void process_all() throws IOException {

        List<String> classes = getFiles(packagePath);

        // default Processor
        process(QuerydslAnnotationProcessor.class, classes,"prefix");

        File file = new File("target/prefix/com/querydsl/apt/domain/query3/QTAddress.java");
        assertTrue(file.exists());

        String content = Files.toString(file, Charsets.UTF_8);

        assertTrue(content.contains("public static final String ENTITY_NAME = \"address\";"));
        assertTrue(content.contains("public static final QTAddress address = new QTAddress(ENTITY_NAME);"));

        assertTrue(content.contains("public static final String POST_CODE = \"postCode\";"));
        assertTrue(content.contains("public final StringPath postCode = createString(POST_CODE);"));
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.namesAsConstants=true", "-Aquerydsl.packageSuffix=.query3", "-Aquerydsl.prefix=QT");
    }

}
