package com.querydsl.jpa.domain17;

import java.io.File;
import java.io.IOException;

import com.querydsl.jpa.codegen.CompileUtils;
import com.querydsl.jpa.codegen.HibernateDomainExporter;
import com.querydsl.core.util.FileUtils;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class DomainExporter17Test {

    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/com/mysema/querydsl/jpa/domain17/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();

        CompileUtils.compile(gen.getAbsolutePath());
    }

}
