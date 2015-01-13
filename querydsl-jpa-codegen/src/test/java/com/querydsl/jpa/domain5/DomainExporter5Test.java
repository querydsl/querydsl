package com.querydsl.jpa.domain5;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.jpa.codegen.CompileUtils;
import com.querydsl.jpa.codegen.HibernateDomainExporter;
import com.querydsl.core.util.FileUtils;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DomainExporter5Test {
    
    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        for (String res : Arrays.asList("Customer.hbm.xml", 
                "CustomerContact.hbm.xml", 
                "CustomerHistory.hbm.xml")) {
            config.addFile(new File("src/test/resources/com/mysema/querydsl/jpa/domain5/" + res));
        }
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();

        File targetFile = new File(gen, "com/mysema/query/jpa/domain5/QCustomer.java");
        assertContains(targetFile, "SetPath<CustomerContact, QCustomerContact>",
                                   "SetPath<CustomerHistory, QCustomerHistory>");

        CompileUtils.compile(gen.getAbsolutePath());
    }


    private static void assertContains(File file, String... strings) throws IOException {
        assertTrue(file.getPath() + " doesn't exist", file.exists());
        String result = Files.toString(file, Charsets.UTF_8);
        for (String str : strings) {
            assertTrue(str + " was not contained", result.contains(str));
        }
    }
    
}
