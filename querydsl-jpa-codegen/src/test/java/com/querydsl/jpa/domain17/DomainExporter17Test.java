package com.querydsl.jpa.domain17;

import java.io.File;
import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.querydsl.core.util.FileUtils;
import com.querydsl.jpa.codegen.CompileUtils;
import com.querydsl.jpa.codegen.HibernateDomainExporter;

public class DomainExporter17Test {

    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/com/querydsl/jpa/domain17/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();

        CompileUtils.compile(gen.getAbsolutePath());
    }

}
