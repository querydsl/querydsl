package com.mysema.query.sql.ant;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class AntMetaDataExporterTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();

    @Test
    public void Execute(){
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriverClass("org.h2.Driver");
        exporter.setDbUserName("sa");
        exporter.setDbUrl(url);
        exporter.setTargetPackage("test");
        exporter.setTargetSourceFolder("target/AntMetaDataExporterTest");
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest").exists());
    }

}
