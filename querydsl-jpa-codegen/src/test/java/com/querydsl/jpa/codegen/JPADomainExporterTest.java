/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa.codegen;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.metamodel.Metamodel;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.metamodel.MetamodelImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.SimpleSerializerConfig;
import com.querydsl.core.annotations.Config;
import com.querydsl.core.util.FileUtils;
import com.querydsl.jpa.domain.Domain;
import com.querydsl.jpa.domain2.Domain2;

public class JPADomainExporterTest {

    private static Properties props = new Properties();

    static {
        try {
            InputStream is = JPADomainExporterTest.class.getResourceAsStream("/h2.properties");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private final SerializerConfig serializerConfig = SimpleSerializerConfig.getConfig(Domain.class
            .getPackage().getAnnotation(Config.class));


    private Metamodel convert(Configuration config) {
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
            .applySettings(props)
            .buildServiceRegistry();

        config.setProperties(props);
        config.buildMappings();
        SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
        return MetamodelImpl.buildMetamodel(config.getClassMappings(),
                (SessionFactoryImplementor) sessionFactory, true);
    }

    @Test
    @Ignore // FIXME
    public void Execute_MyEntity() throws IOException {
        FileUtils.delete(new File("target/jpagen6"));
        File myEntity = new File("src/test/resources/entity.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(myEntity);
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen6"), convert(config));
        exporter.execute();

        File targetFile = new File("target/jpagen6/com/mysema/querydsl/jpa/codegen/QMyEntity.java");
        assertContains(targetFile, "StringPath pk1", "StringPath pk2", "StringPath prop1");
    }

    @Test
    public void Execute_Contact() throws IOException {
        FileUtils.delete(new File("target/jpagen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen1"), convert(config));
        exporter.execute();

        File targetFile = new File("target/jpagen1/com/mysema/querydsl/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");
    }

    @Test
    public void Execute_Contact_with_Suffix() throws IOException {
        FileUtils.delete(new File("target/jpagen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        JPADomainExporter exporter = new JPADomainExporter("", "Type", new File(
                "target/jpagen1"), convert(config));
        exporter.execute();

        File targetFile = new File("target/jpagen1/com/mysema/querydsl/jpa/domain2/ContactType.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");
    }

    @Test
    public void Execute_Contact2() throws IOException {
        FileUtils.delete(new File("target/jpagen2"));
        File contact = new File("src/test/resources/contact2.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen2"), convert(config));
        exporter.execute();

        File targetFile = new File("target/jpagen2/com/mysema/querydsl/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName",
                "NumberPath<Long> id", "StringPath lastName");
    }

    @Test
    public void Execute_Multiple() throws IOException {
        FileUtils.delete(new File("target/jpagen3"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain.classes) {
            config.addAnnotatedClass(cl);
        }
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen3"), serializerConfig, convert(config));
        exporter.execute();

        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/jpagen3/com/mysema/querydsl/jpa/domain").listFiles()) {
            String result1 = Files.toString(file, Charsets.UTF_8);
            String result2 = Files
                    .toString(
                            new File(
                                    "../querydsl-jpa/target/generated-test-sources/java/com/mysema/querydsl/jpa/domain",
                                    file.getName()), Charsets.UTF_8);
            if (!result1.equals(result2)) {
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }

        failures.remove("QCalendar.java"); // FIXME

        if (!failures.isEmpty()) {
            fail("Failed with " + failures.size() + " failures");
        }

    }

    @Test
    public void Execute_Multiple2() throws IOException {
        FileUtils.delete(new File("target/jpagen4"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain2.classes) {
            config.addAnnotatedClass(cl);
        }
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen4"), serializerConfig, convert(config));
        exporter.execute();

        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/jpagen4/com/mysema/querydsl/jpa/domain2").listFiles()) {
            String result1 = Files.toString(file, Charsets.UTF_8);
            String result2 = Files
                    .toString(
                            new File(
                                    "../querydsl-jpa/target/generated-test-sources/java/com/mysema/querydsl/jpa/domain2",
                                    file.getName()), Charsets.UTF_8);
            if (!result1.equals(result2)) {
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }

        if (!failures.isEmpty()) {
            fail("Failed with " + failures.size() + " failures");
        }

    }

    @Test
    @Ignore // FIXME
    public void Execute_Store() throws IOException {
        FileUtils.delete(new File("target/jpagen5"));
        File contact = new File("src/test/resources/store.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        JPADomainExporter exporter = new JPADomainExporter("Q",
                new File("target/jpagen5"), convert(config));
        exporter.execute();

        File targetFile = new File("target/jpagen5/com/mysema/querydsl/jpa/domain3/QStore.java");
        assertContains(targetFile, "StringPath code", "StringPath address");

        targetFile = new File("target/jpagen5/com/mysema/querydsl/jpa/domain3/QHardwareStore.java");
        assertContains(targetFile, "StringPath code = _super.code;", "StringPath address");
    }

    private static void assertContains(File file, String... strings) throws IOException {
        assertTrue(file.getPath() + " doesn't exist", file.exists());
        String result = Files.toString(file, Charsets.UTF_8);
        for (String str : strings) {
            assertTrue(str + " was not contained", result.contains(str));
        }
    }

}
