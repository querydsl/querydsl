package com.querydsl.jpa.codegen.ant;

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

        // Verify that the Querydsl querydsl type was created successfully.
        assertTrue(new File("target/AntJPADomainExporterTest/com/mysema/querydsl/jpa/ant/QDepartments.java").exists());
    }

}
