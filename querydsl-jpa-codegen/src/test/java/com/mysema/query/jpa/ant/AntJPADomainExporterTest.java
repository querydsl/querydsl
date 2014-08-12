/**
 * Copyright 2014, General Electric Company
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
package com.mysema.query.jpa.ant;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AntJPADomainExporterTest {

    @Test
    public void testExecute() {
        // Simulate configuring the task in an Ant build.xml file.
        AntJPADomainExporter exporter = new AntJPADomainExporter();
        exporter.setNamePrefix("Q");
        exporter.setNameSuffix("");
        exporter.setTargetFolder("target/AntJPADomainExporterTest");
        exporter.setPersistenceUnitName("AntJPADomainExporterTest");
        exporter.execute();

        // Verify that the Querydsl query type was created successfully.
        assertTrue(new File("target/AntJPADomainExporterTest/com/mysema/query/jpa/ant/QDepartments.java").exists());
    }

}
