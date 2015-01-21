package com.querydsl.jpa.codegen;

import java.io.File;

import org.junit.Test;

import com.querydsl.codegen.GenericExporter;

public class GenericExporterTest {

    @Test
    public void test() {
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new File("target/" + GenericExporterTest.class.getSimpleName()));
        exporter.export(getClass().getPackage());
    }
    
}
