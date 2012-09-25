package com.mysema.query.jpa.domain9;

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
        config.addFile(new File("src/test/resources/com/mysema/query/jpa/domain9/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();
    }
    
}
