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
package com.mysema.query.jpa.hibernate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import com.mysema.query.annotations.Config;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.jpa.domain.Domain;
import com.mysema.query.jpa.domain2.Domain2;

public class HibernateDomainExporterTest {
    
    private final SerializerConfig serializerConfig = SimpleSerializerConfig.getConfig(Domain.class.getPackage().getAnnotation(Config.class));
        
    @Test
    public void Execute_Contact() throws IOException {
        FileUtils.deleteDirectory(new File("target/gen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen1"), config);
        exporter.execute();
        
        File targetFile = new File("target/gen1/com/mysema/query/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName", "NumberPath<Long> id", "StringPath lastName");
    }
    
    @Test
    public void Execute_Contact_with_Suffix() throws IOException {
        FileUtils.deleteDirectory(new File("target/gen1"));
        File contact = new File("src/test/resources/contact.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("", "Type", new File("target/gen1"), config);
        exporter.execute();
        
        File targetFile = new File("target/gen1/com/mysema/query/jpa/domain2/ContactType.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName", "NumberPath<Long> id", "StringPath lastName");
    }
    
    @Test
    public void Execute_Contact2() throws IOException {
        FileUtils.deleteDirectory(new File("target/gen2"));
        File contact = new File("src/test/resources/contact2.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen2"), config);
        exporter.execute();
        
        File targetFile = new File("target/gen2/com/mysema/query/jpa/domain2/QContact.java");
        assertContains(targetFile, "StringPath email", "StringPath firstName", "NumberPath<Long> id", "StringPath lastName");
    }
    
    @Test
    public void Execute_Multiple() throws IOException{
        FileUtils.deleteDirectory(new File("target/gen3"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain.classes){
            config.addAnnotatedClass(cl);
        }        
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen3"), serializerConfig, config);
        exporter.execute();
        
        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/gen3/com/mysema/query/jpa/domain").listFiles()){
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(
                new File("target/generated-test-sources/java/com/mysema/query/jpa/domain", file.getName()));
            if (!result1.equals(result2)){
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }
        
        if (!failures.isEmpty()){
            fail("Failed with " + failures.size() + " failures");
        }
        
    }
    
    @Test
    public void Execute_Multiple2() throws IOException{
        FileUtils.deleteDirectory(new File("target/gen4"));
        Configuration config = new Configuration();
        for (Class<?> cl : Domain2.classes){
            config.addAnnotatedClass(cl);
        }        
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen4"), serializerConfig, config);
        exporter.execute();
        
        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/gen4/com/mysema/query/jpa/domain2").listFiles()){
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(
                new File("target/generated-test-sources/java/com/mysema/query/jpa/domain2", file.getName()));
            if (!result1.equals(result2)){
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }
        
        if (!failures.isEmpty()){
            fail("Failed with " + failures.size() + " failures");
        }
        
    }
    

    @Test
    public void Execute_Store() throws IOException {
        FileUtils.deleteDirectory(new File("target/gen5"));
        File contact = new File("src/test/resources/store.hbm.xml");
        Configuration config = new Configuration();
        config.addFile(contact);
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen5"), config);
        exporter.execute();
        
        File targetFile = new File("target/gen5/com/mysema/query/jpa/domain3/QStore.java");
        assertContains(targetFile, "StringPath code", "StringPath address");
        
        targetFile = new File("target/gen5/com/mysema/query/jpa/domain3/QHardwareStore.java");
        assertContains(targetFile, "StringPath code = _super.code;", "StringPath address");
    }

    private static void assertContains(File file, String... strings) throws IOException{
        assertTrue(file.getPath() + " doesn't exist", file.exists());
        String result = FileUtils.readFileToString(file, "UTF-8");
        for (String str : strings){
            assertTrue(str + " was not contained", result.contains(str));    
        }
    }

}
