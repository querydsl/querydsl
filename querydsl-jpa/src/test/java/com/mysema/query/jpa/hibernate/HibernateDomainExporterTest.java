package com.mysema.query.jpa.hibernate;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class HibernateDomainExporterTest {

    @Test
    public void Execute() throws IOException {
        File contact = new File("src/test/resources/contact.hbm.xml");
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen"), contact);
        exporter.execute();
        
        File targetFile = new File("target/gen/com/mysema/example/QContact.java");
        assertTrue(targetFile.exists());
        String result = FileUtils.readFileToString(targetFile, "UTF-8");
        System.out.println(result);
        assertTrue(result.contains("StringPath firstName"));
        assertTrue(result.contains("StringPath lastName"));
    }

}
