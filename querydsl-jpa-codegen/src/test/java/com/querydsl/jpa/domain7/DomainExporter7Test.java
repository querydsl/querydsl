package com.querydsl.jpa.domain7;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.querydsl.core.util.FileUtils;
import com.querydsl.jpa.codegen.CompileUtils;
import com.querydsl.jpa.codegen.HibernateDomainExporter;

public class DomainExporter7Test {

    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/com/querydsl/jpa/domain7/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();

        assertTrue(new File(gen, "com/querydsl/jpa/domain7/QA.java").exists());
        assertTrue(new File(gen, "com/querydsl/jpa/domain7/QB.java").exists());
        assertTrue(new File(gen, "com/querydsl/jpa/domain7/QC.java").exists());

        CompileUtils.compile(gen.getAbsolutePath());
    }

}
