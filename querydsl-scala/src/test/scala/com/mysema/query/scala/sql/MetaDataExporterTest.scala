package com.mysema.query.scala.sql

import org.apache.commons.lang3.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;

import com.mysema.query.codegen._;
import com.mysema.query.sql._

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

import com.mysema.query.scala._

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
            
            // multi key
            stmt.execute("create table multikey(id INT, id2 VARCHAR, id3 INT, CONSTRAINT pk_multikey PRIMARY KEY (id, id2, id3) )");
        } finally {
            stmt.close();
        }
    }
    
    @Test
    def GenerateWithoutBeanTypes() {
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
    def GenerateWithBeanTypes() {
        val directory = new java.io.File("target/jdbcgen2");
        val namingStrategy = new DefaultNamingStrategy();
        val beanSerializer = new ScalaBeanSerializer();
        val exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("com.mysema");
        exporter.setSchemaPattern("PUBLIC");
        exporter.setTargetFolder(directory);
        exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
        exporter.setBeanSerializer(beanSerializer)
        exporter.setCreateScalaSources(true);
        exporter.setTypeMappings(ScalaTypeMappings.create);
        exporter.export(connection.getMetaData);
        
        assertCompileSuccess(recursiveFileList(directory))
        
    }
    
}