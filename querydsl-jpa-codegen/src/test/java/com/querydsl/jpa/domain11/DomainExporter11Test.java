package com.querydsl.jpa.domain11;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.jpa.codegen.CompileUtils;
import com.querydsl.jpa.codegen.HibernateDomainExporter;
import com.querydsl.core.util.FileUtils;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DomainExporter11Test {

    @Test
    public void Execute() throws IOException {
        File gen = new File("target/" + getClass().getSimpleName());
        FileUtils.delete(gen);
        Configuration config = new Configuration();
        config.addFile(new File("src/test/resources/com/mysema/querydsl/jpa/domain11/domain.hbm.xml"));
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", gen, config);
        exporter.execute();
        
        assertTrue(new File(gen, "com/mysema/query/jpa/domain11/QOtherthing.java").exists());
        assertTrue(new File(gen, "com/mysema/query/jpa/domain11/QSomething.java").exists());
        
        String str = Files.toString(new File(gen, "com/mysema/query/jpa/domain11/QOtherthing.java"), Charsets.UTF_8);
        assertTrue(str.contains("QSomething"));
        
        str = Files.toString(new File(gen, "com/mysema/query/jpa/domain11/QSomething.java"), Charsets.UTF_8);
        assertTrue(str.contains("id"));

        CompileUtils.compile(gen.getAbsolutePath());
    }
    
}
