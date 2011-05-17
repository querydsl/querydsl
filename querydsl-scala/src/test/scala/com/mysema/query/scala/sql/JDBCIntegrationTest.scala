package com.mysema.query.scala.sql

import test._
import com.mysema.query.sql._
import com.mysema.query.types.path._
import java.io.File

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

import org.junit._
import org.junit.Assert._

import java.util.Arrays

import com.mysema.query.scala.CompileTestUtils

import com.mysema.query.scala.ScalaBeanSerializer
import com.mysema.query.scala.ScalaTypeMappings

import com.mysema.query.sql.dml._

class JDBCIntegrationTest extends CompileTestUtils {
    
  val survey = new QSurvey("survey")
  val employee = new QEmployee("employee")

  val templates = new HSQLDBTemplates()

  var connection: Connection = _

  var statement: Statement = _

  @Before
  def setUp() {
    Class.forName("org.h2.Driver")
    val url = "jdbc:h2:target/h2"
    
    connection = DriverManager.getConnection(url, "sa", "")
    statement = connection.createStatement()
    statement.execute("drop table employee if exists")
    statement.execute("drop table survey if exists")
    statement.execute("drop table date_test if exists")
    statement.execute("drop table date_time_test if exists")

    statement.execute("""create table survey (
      id int identity,
      name varchar(30))""")

    statement.execute("insert into survey (name) values ('abc')")
    statement.execute("insert into survey (name) values ('def')")

    statement.execute("""create table employee(
      id INT identity,
      firstname VARCHAR(50),
      lastname VARCHAR(50),
      superior_id int,
      CONSTRAINT PK_employee PRIMARY KEY (id),
      CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))""")

    statement.execute("insert into employee (firstname, lastname) values ('Bob', 'Smith')")
    statement.execute("insert into employee (firstname, lastname) values ('John', 'Doe')")
    
    // TODO : create table with multi column primary key
  }

  @Test
  def Generation_without_Beantypes {
    val namingStrategy = new DefaultNamingStrategy()
    val exporter = new MetaDataExporter()
    exporter.setNamePrefix("Q")
    exporter.setPackageName("test")
    val directory = new File("target/gen1")
    exporter.setTargetFolder(directory)
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer])
    exporter.setCreateScalaSources(true)
    exporter.setTypeMappings(ScalaTypeMappings.create)
    exporter.export(connection.getMetaData)

    assertCompileSuccess(recursiveFileList(directory))
  }
  
  private def recursiveFileList(file: File): Array[File] = {
    if (file.isDirectory) {
      file.listFiles.flatMap(recursiveFileList(_))
    } else {
      Array(file)
    }
  }

  @Test
  def Generation_with_Beantypes {
    val namingStrategy = new DefaultNamingStrategy()
    val beanSerializer = new ScalaBeanSerializer()
    val exporter = new MetaDataExporter()
    exporter.setNamePrefix("Q")
    exporter.setPackageName("test")
    exporter.setTargetFolder(new File("target/gen2"))
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer])
    exporter.setBeanSerializer(beanSerializer)
    exporter.setCreateScalaSources(true)
    exporter.setTypeMappings(ScalaTypeMappings.create)
    exporter.export(connection.getMetaData)
    
    // TODO : compile sources
  }

  @Test
  def Populate_Bean {
    assertEquals(2, query from (survey) list (survey) size ())
  }
  
  @Test
  def List {
    assertEquals(2, query from (survey) list (survey.id) size ())
    assertEquals(2, query from (employee) list (employee.firstname) size ())
  }
  
  @Test
  def Count {
    assertEquals(2, query from (survey) count)
    assertEquals(2, query from (employee) count)
  }
  
  @Test
  def Unique_Result {
    assertEquals("abc", query from survey where (survey.id eq 1) uniqueResult survey.name)
    assertEquals("def", query from survey where (survey.id eq 2) uniqueResult survey.name)
    assertEquals("Bob", query from employee where (employee.lastname eq "Smith") uniqueResult employee.firstname)
    assertEquals("John", query from employee where (employee.lastname eq "Doe") uniqueResult employee.firstname)
  }  
  
  @Test
  def Insert {
      val s = new Survey()
      s.name = "XXX"
          
      val id = insert(survey) populate(s) executeWithKey(survey.id)
      val sNew = query from survey where (survey.id === id) uniqueResult (survey)
      assertEquals(s.name, sNew.name)
  }
  
  @Test
  def Update {
      val s = new Survey()
      s.name = "XXX"
          
      val id = insert(survey) populate(s) executeWithKey(survey.id)
      s.id = id
      s.name = "YYY"
      
      val count = update(survey) populate(s) execute()
      assertTrue(count > 0)
      
      val sNew = query from survey where (survey.id === id) uniqueResult (survey)
      assertEquals(s.name, sNew.name)
  }
  
  @Test
  def Delete {
      val s = new Survey()
      s.name = "XXX"
          
      val id = insert(survey) populate(s) executeWithKey(survey.id)      
      val count = delete(survey) where(survey.id === id) execute()
      assertTrue(count > 0)
  }
  

  @After
  def tearDown() {
    try {
      statement.close()
    } finally {
      connection.close()
    }
  }

  def query = new SQLQueryImpl(connection, templates)

  def delete(path: RelationalPath[_]) = new SQLDeleteClause(connection, templates, path)
  
  def insert(path: RelationalPath[_]) = new SQLInsertClause(connection, templates, path)
  
  def update(path: RelationalPath[_]) = new SQLUpdateClause(connection, templates, path)
  
}
