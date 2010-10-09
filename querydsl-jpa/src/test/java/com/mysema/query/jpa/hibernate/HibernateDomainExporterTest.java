package com.mysema.query.jpa.hibernate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.mysema.commons.fluxml.XMLWriter;
import com.mysema.query.annotations.Config;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.SimpleSerializerConfig;
import com.mysema.query.jpa.domain.Domain;

public class HibernateDomainExporterTest {
    
    private SerializerConfig serializerConfig = SimpleSerializerConfig.getConfig(Domain.class.getPackage().getAnnotation(Config.class));
    
    @Test
    public void Execute_Single() throws IOException {
        File contact = new File("src/test/resources/contact.hbm.xml");
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen1"), contact);
        exporter.execute();
        
        File targetFile = new File("target/gen1/com/mysema/example/QContact.java");
        assertContains(targetFile, "StringPath firstName", "StringPath lastName");
    }
    
    @Test
    public void Execute_Single2() throws IOException {
        File contact = new File("src/test/resources/contact2.hbm.xml");
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen2"), contact);
        exporter.execute();
        
        File targetFile = new File("target/gen2/com/mysema/example/QContact.java");
        assertContains(targetFile, "StringPath firstName", "StringPath lastName");
    }
    
    @Test
    public void Execute_Multiple() throws IOException{
        File out = new File("target/out.hbm.xml");
        out.delete();
        createExampleHbmFile(out);
        
        HibernateDomainExporter exporter = new HibernateDomainExporter("Q", new File("target/gen3"), serializerConfig, out);
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

    private void createExampleHbmFile(File out) throws IOException {
        // NOTE : the output is not a valid hbm file, but the relevant data for parsing is contained
        Writer w = new FileWriter(out);
        XMLWriter writer = new XMLWriter(w);
        writer.begin("hibernate-mapping");
        for (Class<?> cl : Domain.classes){
            if (cl.isEnum()) continue;
            String classElement;
            if (cl.getAnnotation(Entity.class) != null){
                classElement = "class";
            }else if (cl.getAnnotation(MappedSuperclass.class) != null){
                classElement = "mapped-superclass";
            }else{
                continue;
            }
            writer.begin(classElement);
            writer.attribute("name", cl.getName());
            for (Field field : cl.getDeclaredFields()){
                if (field.getAnnotation(Transient.class) != null) continue;
                if (Modifier.isTransient(field.getModifiers())) continue;
                String propertyElement = getPropertyElement(field.getType());
                writer.begin(propertyElement);
                writer.attribute("name", field.getName());
                if (field.getType().getAnnotation(Embeddable.class) != null){
                    for (Field cfield : field.getType().getDeclaredFields()){
                        String cproperty = getPropertyElement(cfield.getType());
                        writer.begin(cproperty);
                        writer.attribute("name", cfield.getName());
                        writer.end(cproperty);
                    }
                }
                writer.end(propertyElement);
            }
            writer.end(classElement);
        }        
        writer.end("hibernate-mapping");
        w.close();
    }
    
    private String getPropertyElement(Class<?> cl){
        if (cl.getAnnotation(Entity.class) != null){
            return "many-to-one";
        }else if (cl.getAnnotation(Embeddable.class) != null){
            return "component";
        }else{
            return "property";
        }
    }
    
    private static void assertContains(File file, String... strings) throws IOException{
        assertTrue(file.getPath() + " doesn't exist", file.exists());
        String result = FileUtils.readFileToString(file, "UTF-8");
//        System.out.println(result);
        for (String str : strings){
            assertTrue(str + " was not contained", result.contains(str));    
        }
    }

}
