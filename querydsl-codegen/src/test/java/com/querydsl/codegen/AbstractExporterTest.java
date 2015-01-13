package com.querydsl.codegen;

import java.io.File;

import org.junit.Test;

public abstract class AbstractExporterTest {
    
    @Test
    public void test() {
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new File("target/" + getClass().getSimpleName()));
        exporter.export(getClass().getClasses());
    }

}
