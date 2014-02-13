package com.mysema.query.jpa.domain16;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.mysema.query.jpa.codegen.HibernateDomainExporter;
import com.mysema.util.FileUtils;

public class DomainExporterTest {

    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/com/mysema/query/jpa/domain16/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();

        assertTrue(new File(gen, "com/mysema/query/jpa/domain16/QEntity.java").exists());
        assertFalse(new File(gen, "com/mysema/query/jpa/domain16/QCustom.java").exists());
        assertTrue(new File(gen, "com/mysema/query/jpa/domain16/QCustom2.java").exists());
        assertFalse(new File(gen, "com/mysema/query/jpa/domain16/QCustom3.java").exists());

    }

}
