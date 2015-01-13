package com.querydsl.scala.sql

import com.mysema.codegen._
import com.mysema.codegen.model._

import com.querydsl.codegen._
import com.querydsl.sql._
import com.querydsl.sql.codegen._

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

import com.querydsl.scala._

class MetaDataExporterTest extends CompileTestUtils {
        
    var connection: java.sql.Connection = _;
    
    @Before
    def setUp() {
        Class.forName("org.h2.Driver");
        val url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();
        connection = java.sql.DriverManager.getConnection(url, "sa", "");

        val stmt = connection.createStatement();

        try{
            stmt.execute("create table reserved (id int, while int)");
            stmt.execute("create table underscore (e_id int, c_id int)");
            stmt.execute("create table beangen1 (\"SEP_Order\" int)");
            stmt.execute("create table definstance (id int, definstance int, definstance1 int)");
            stmt.execute("create table pkfk (id int primary key, pk int, fk int)");
            stmt.execute("create table \"camelCase\" (id int)");
            stmt.execute("create table \"vwServiceName\" (id int)");
            stmt.execute("create table date_test (d date)");
            stmt.execute("create table date_time_test (dt datetime)");
            stmt.execute("create table survey (id int, name varchar(30))");
            stmt.execute("create table typetest (type int, constraint pk_typetest primary key(type))");
            
            stmt.execute("""create table employee(
                    id INT, firstname VARCHAR(50), lastname VARCHAR(50), salary DECIMAL(10, 2), 
                    datefield DATE, timefield TIME, 
                    superior_id int, survey_id int, survey_name varchar(30), 
                    CONSTRAINT PK_employee PRIMARY KEY (id), 
                    CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))""");
            
                        
            // table with count column
            stmt.execute("create table count_table(count int)");
            
            // multi primary key
            stmt.execute("create table multikey(id INT, id2 VARCHAR, id3 INT," +
            		" CONSTRAINT pk_multikey PRIMARY KEY (id, id2, id3) )");
            
            // multi foreign key
            stmt.execute("create table multikey2(id INT, id2 INT, id3 INT, id4 INT, id5 INT, id6 INT," +
            		" CONSTRAINT pk_multikey2 FOREIGN KEY (id4, id5, id6) REFERENCES multikey2(id, id2, id3) )");
            
        } finally {
            stmt.close();
        }
    }
    
    @Test
    def Generate_Without_BeanTypes() {
        val directory = new java.io.File("target/jdbcgen1");
        val namingStrategy = new DefaultNamingStrategy();
        val exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("com.mysema");
        exporter.setSchemaPattern("PUBLIC");
        exporter.setTargetFolder(directory);
        exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
        exporter.setCreateScalaSources(true);
        exporter.setTypeMappings(ScalaTypeMappings.create);
        exporter.export(connection.getMetaData);
        
        assertCompileSuccess(recursiveFileList(directory))
    }
    
    @Test
    def Generate_With_BeanTypes() {
        val directory = new java.io.File("target/jdbcgen2");
        val namingStrategy = new DefaultNamingStrategy();
        //val beanSerializer = new ScalaBeanSerializer();
        val exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("com.mysema");
        exporter.setSchemaPattern("PUBLIC");
        exporter.setTargetFolder(directory);
        exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
        exporter.setBeanSerializerClass(classOf[ScalaBeanSerializer]);
        exporter.setCreateScalaSources(true);
        exporter.setTypeMappings(ScalaTypeMappings.create);
        exporter.export(connection.getMetaData);
        
        assertCompileSuccess(recursiveFileList(directory))        
    }
    
    @Test
    def Generate_With_Schema() {
        val directory = new java.io.File("target/jdbcgen3");
        val namingStrategy = new DefaultNamingStrategy();
        //val beanSerializer = new ScalaBeanSerializer();
        val exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("com.mysema");
        exporter.setSchemaPattern("PUBLIC");
        exporter.setSchemaToPackage(true);
        exporter.setTargetFolder(directory);
        exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
        exporter.setCreateScalaSources(true);
        exporter.setTypeMappings(ScalaTypeMappings.create);
        exporter.export(connection.getMetaData);
        
        assertCompileSuccess(recursiveFileList(directory))       
    }    
    
    @Test
    def Generate_With_BeanTypes_And_Schema() {
              val directory = new java.io.File("target/jdbcgen4");
        val namingStrategy = new DefaultNamingStrategy();
        //val beanSerializer = new ScalaBeanSerializer();
        val exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("com.mysema");
        exporter.setSchemaPattern("PUBLIC");
        exporter.setSchemaToPackage(true);
        exporter.setTargetFolder(directory);
        exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
        exporter.setBeanSerializerClass(classOf[ScalaBeanSerializer]);
        exporter.setCreateScalaSources(true);
        exporter.setTypeMappings(ScalaTypeMappings.create);
        exporter.export(connection.getMetaData);
        
        assertCompileSuccess(recursiveFileList(directory))       
    }
    
}