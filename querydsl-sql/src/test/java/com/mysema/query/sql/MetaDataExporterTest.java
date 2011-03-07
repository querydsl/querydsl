/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.mysema.codegen.SimpleCompiler;
import com.mysema.query.AbstractJDBCTest;
import com.mysema.query.codegen.BeanSerializer;

/**
 * MetaDataExporterTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporterTest extends AbstractJDBCTest{
    
    private static final NamingStrategy defaultNaming = new DefaultNamingStrategy();

    private static final NamingStrategy originalNaming = new OriginalNamingStrategy();

    private String beanPackageName = null;
    
    @Test
    public void NormalSettings() throws SQLException{
        test("Q", "", "", "", defaultNaming, "target/1", false, false);
        
        assertTrue(new File("target/1/test/QEmployee.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Beans() throws SQLException{
        test("Q", "", "", "", defaultNaming, "target/11", true, false);
        
        assertTrue(new File("target/11/test/QEmployee.java").exists());
        assertTrue(new File("target/11/test/Employee.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Beans_and_extra_Package() throws SQLException{
        beanPackageName = "test2";
        test("Q", "", "", "", defaultNaming, "target/11_extraPackage", true, false);
        
        assertTrue(new File("target/11_extraPackage/test/QEmployee.java").exists());
        assertTrue(new File("target/11_extraPackage/test2/Employee.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Beans_with_Bean_prefix() throws SQLException{
        test("Q", "", "Bean", "", defaultNaming, "target/11a", true, false);
        
        assertTrue(new File("target/11a/test/QEmployee.java").exists());
        assertTrue(new File("target/11a/test/BeanEmployee.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Beans_with_Bean_suffix() throws SQLException{
        test("Q", "", "", "Bean", defaultNaming, "target/11b", true, false);
        
        assertTrue(new File("target/11b/test/QEmployee.java").exists());
        assertTrue(new File("target/11b/test/EmployeeBean.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Suffix() throws SQLException{
        test("Q", "Type", "", "", defaultNaming, "target/1_suffix", false, false);
        
        assertTrue(new File("target/1_suffix/test/QEmployeeType.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Suffix_with_Beans() throws SQLException{
        test("", "Type", "", "", defaultNaming, "target/11_suffix", true, false);
        
        assertTrue(new File("target/11_suffix/test/Employee.java").exists());
        assertTrue(new File("target/11_suffix/test/EmployeeType.java").exists());
    }
    
    @Test
    public void NormalSettings_with_Suffix_with_Beans_and_extra_package() throws SQLException{
        beanPackageName = "test2";
        test("", "Type", "", "", defaultNaming, "target/11_suffix_extra", true, false);
        
        assertTrue(new File("target/11_suffix_extra/test/EmployeeType.java").exists());
        assertTrue(new File("target/11_suffix_extra/test2/Employee.java").exists());
    }
        
    @Test
    public void NormalSettings_with_Suffix_with_Beans_with_prefix() throws SQLException{
        test("", "Type", "Bean", "", defaultNaming, "target/11_suffixa", true, false);
        
        assertTrue(new File("target/11_suffixa/test/EmployeeType.java").exists());
        assertTrue(new File("target/11_suffixa/test/BeanEmployee.java").exists());
    }

    @Test
    public void NormalSettings_with_Suffix_with_Beans_with_suffix() throws SQLException{
        test("", "Type", "", "Bean", defaultNaming, "target/11_suffixb", true, false);
        
        assertTrue(new File("target/11_suffixb/test/EmployeeType.java").exists());
        assertTrue(new File("target/11_suffixb/test/EmployeeBean.java").exists());
    }

    @Test
    public void NormalSettings_with_InnerClasses() throws SQLException{
        test("Q", "", "", "", defaultNaming, "target/1_with_InnerClasses", false, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_with_Beans() throws SQLException{
        test("Q", "", "", "", defaultNaming, "target/11_with_InnerClasses", true, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_with_Beans_with_prefix() throws SQLException{
        test("Q", "", "Bean", "", defaultNaming, "target/11_with_InnerClassesa", true, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_with_Beans_with_suffix() throws SQLException{
        test("Q", "", "", "Bean", defaultNaming, "target/11_with_InnerClassesb", true, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_and_Suffix() throws SQLException{
        test("Q", "Type", "", "", defaultNaming, "target/1_with_InnerClasses_and_Suffix", false, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_and_Suffix_with_Beans() throws SQLException{
        test("", "Type", "", "", defaultNaming, "target/11_with_InnerClasses_and_Suffix", true, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_and_Suffix_with_Beans_with_prefix() throws SQLException{
        test("", "Type", "Bean", "", defaultNaming, "target/11_with_InnerClasses_and_Suffixa", true, true);
    }
    
    @Test
    public void NormalSettings_with_InnerClasses_and_Suffix_with_Beans_with_suffix() throws SQLException{
        test("", "Type", "", "Bean", defaultNaming, "target/11_with_InnerClasses_and_Suffixb", true, true);
    }

    @Test
    public void WithoutPrefix() throws SQLException{
        test("", "", "", "", defaultNaming, "target/2", false, false);
        
        assertTrue(new File("target/2/test/Employee.java").exists());
    }

    @Test
    public void WithoutPrefix_with_InnerClasses() throws SQLException{
        test("", "", "", "", defaultNaming, "target/2_with_InnerClasses", false, true);
    }

    @Test
    public void WithLongPrefix() throws SQLException{
        test("QDSL", "", "","", defaultNaming, "target/3", false, false);
        
        assertTrue(new File("target/3/test/QDSLEmployee.java").exists());
    }

    @Test
    public void WithLongPrefix_with_InnerClasses() throws SQLException{
        test("QDSL", "", "","", defaultNaming, "target/3_with_InnerClasses", false, true);
    }

    @Test
    public void WithDifferentNamingStrategy() throws SQLException{
        test("Q", "", "","", originalNaming, "target/4", false, false);
    }
    
    @Test
    public void WithDifferentNamingStrategy_and_Suffix() throws SQLException{
        test("Q", "Type", "","", originalNaming, "target/4_suffix", false, false);
    }

    @Test
    public void WithDifferentNamingStrategy_with_InnerClasses() throws SQLException{
        test("Q", "", "","", originalNaming, "target/4_with_InnerClasses", false, true);
    }

    @Test
    public void WithoutPrefix2() throws SQLException{
        test("", "", "", "", originalNaming, "target/5", false, false);
    }

    @Test
    public void WithoutPrefix2_with_InnerClasses() throws SQLException{
        test("", "", "", "", originalNaming, "target/5_with_InnerClasses", false, true);
    }

    @Test
    public void WithLongPrefix2() throws SQLException{
        test("QDSL", "", "", "", originalNaming, "target/6", false, false);
    }

    @Test
    public void WithLongPrefix2_with_InnerClasses() throws SQLException{
        test("QDSL", "", "", "", originalNaming, "target/6_with_InnerClasses", false, true);
    }


    @Test
    public void Explicit_Configuration() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/7"));
        exporter.setNamingStrategy(new DefaultNamingStrategy());
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.export(connection.getMetaData());
    }

    @Test
    public void Minimal_Configuration() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/8"));
        exporter.export(connection.getMetaData());
    }
    
    @Test
    public void Minimal_Configuration_with_Suffix() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setNameSuffix("Type");
        exporter.setTargetFolder(new File("target/9"));
        exporter.export(connection.getMetaData());
    }

    @Test
    public void Minimal_Configuration_with_Bean_prefix() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setBeanPrefix("Bean");
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setTargetFolder(new File("target/a"));
        exporter.export(connection.getMetaData());
    }

    @Test
    public void Minimal_Configuration_with_Bean_suffix() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setTargetFolder(new File("target/b"));
        exporter.export(connection.getMetaData());
    }
    
    private void test(String namePrefix, String nameSuffix, String beanPrefix, String beanSuffix, NamingStrategy namingStrategy, String target, boolean withBeans, boolean withInnerClasses) throws SQLException{
        statement.execute("drop table employee if exists");

        // reserved words
        statement.execute("drop table reserved if exists");
        statement.execute("create table reserved (id int, while int)");

        // default instance clash
        statement.execute("drop table definstance if exists");
        statement.execute("create table definstance (id int, definstance int, definstance1 int)");
        
        // class with pk and fk classes
        statement.execute("drop table pkfk if exists");
        statement.execute("create table pkfk (id int primary key, pk int, fk int)");

        // camel case
        statement.execute("drop table \"camelCase\" if exists");
        statement.execute("create table \"camelCase\" (id int)");

        statement.execute("drop table \"vwServiceName\" if exists");
        statement.execute("create table \"vwServiceName\" (id int)");

        // simple types
        statement.execute("drop table date_test if exists");
        statement.execute("create table date_test (d date)");

        statement.execute("drop table date_time_test if exists");
        statement.execute("create table date_time_test (dt datetime)");

        // complex type
//        statement.execute("drop table employee if exists");

        statement.execute("drop table survey if exists");
        statement.execute("create table survey (id int, name varchar(30))");

        statement.execute("create table employee("
                + "id INT, "
                + "firstname VARCHAR(50), "
                + "lastname VARCHAR(50), "
                + "salary DECIMAL(10, 2), "
                + "datefield DATE, "
                + "timefield TIME, "
                + "superior_id int, "
                + "survey_id int, "
                + "survey_name varchar(30), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");

        
        File targetDir = new File(target);
        try {
            if (targetDir.exists()){
                FileUtils.cleanDirectory(targetDir);    
            }            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setNameSuffix(nameSuffix);
        exporter.setBeanPrefix(beanPrefix);
        exporter.setBeanSuffix(beanSuffix);
        exporter.setInnerClassesForKeys(withInnerClasses);
        exporter.setPackageName("test");
        exporter.setBeanPackageName(beanPackageName);
        exporter.setTargetFolder(targetDir);
        exporter.setNamingStrategy(namingStrategy);
        if (withBeans){
            exporter.setBeanSerializer(new BeanSerializer());
        }
        exporter.export(connection.getMetaData());

        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, System.out, System.err, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }

}
