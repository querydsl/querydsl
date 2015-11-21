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
package com.querydsl.jpa.codegen;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class JPADomainExporterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ErrorCollector errors = new ErrorCollector();

    @Test
    public void test() throws IOException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2", new Properties());
        JPADomainExporter exporter = new JPADomainExporter(folder.getRoot(), emf.getMetamodel());
        exporter.execute();

        File origRoot = new File("../querydsl-jpa/target/generated-test-sources/java");
        Set<File> files = exporter.getGeneratedFiles();
        assertFalse(files.isEmpty());
        for (File file : files) {
            String path = file.getAbsolutePath().replace(
                    folder.getRoot().getAbsolutePath(), origRoot.getAbsolutePath());
            String reference = Files.toString(new File(path), Charsets.UTF_8);
            String content = Files.toString(file, Charsets.UTF_8);
            errors.checkThat("Mismatch for " + file.getPath(), content, is(equalTo(reference)));
        }
    }

}
