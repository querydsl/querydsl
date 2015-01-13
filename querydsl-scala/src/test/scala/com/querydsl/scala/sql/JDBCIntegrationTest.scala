package com.querydsl.scala.sql

import test._
import com.querydsl.sql._
import com.querydsl.core.types.path._
import java.io.File

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

import org.junit._
import org.junit.Assert._

import java.util.Arrays

import com.querydsl.scala.CompileTestUtils

import com.querydsl.scala.ScalaBeanSerializer
import com.querydsl.scala.ScalaTypeMappings

import com.querydsl.sql.codegen._
import com.querydsl.sql.dml._
import com.querydsl.scala.Helpers._

class JDBCIntegrationTest extends CompileTestUtils with SQLHelpers {
    
  val survey = QSurvey
  val employee = QEmployee

  val templates = new HSQLDBTemplates()

  var connection: Connection = _

  var statement: Statement = _
  
  @Before
  def setUp() {
    Class.forName("org.h2.Driver")
    val url = "jdbc:h2:~/dbs/h2-scala"
    
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
    exporter.setPackageName("com.mysema")
    val directory = new File("target/gensql1")
    exporter.setTargetFolder(directory)
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer])
    exporter.setCreateScalaSources(true)
    exporter.setTypeMappings(ScalaTypeMappings.create)
    exporter.setSchemaPattern("PUBLIC")
    exporter.export(connection.getMetaData)

    assertCompileSuccess(recursiveFileList(directory))
  }

  @Test
  def Generation_with_Beantypes {
    val namingStrategy = new DefaultNamingStrategy()
    //val beanSerializer = new ScalaBeanSerializer()
    val exporter = new MetaDataExporter()
    exporter.setNamePrefix("Q")
    exporter.setPackageName("com.mysema")
    val directory = new File("target/gensql2")
    exporter.setTargetFolder(directory)
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer])
    exporter.setBeanSerializerClass(classOf[ScalaBeanSerializer])
    exporter.setCreateScalaSources(true)
    exporter.setTypeMappings(ScalaTypeMappings.create)
    exporter.setSchemaPattern("PUBLIC")
    exporter.export(connection.getMetaData)
    
    assertCompileSuccess(recursiveFileList(directory))
  }
    
  @Test
  def Populate_Bean {
    assertEquals(2, query.from(survey).list(survey) size ())
  }
  
  @Test
  def Populate_Bean_via_Factory {
    assertEquals(2, query.from(survey).list(create[Survey](survey.id, survey.name)) size ())
  }
  
  @Test
  def List {
    assertEquals(2, query.from(survey).list(survey.id) size ())
    assertEquals(2, query.from(employee).list(employee.firstname) size ())
  }
  
  @Test
  def List_2 {
    assertEquals(2, survey.select(_.id) size)
    assertEquals(2, employee.select(_.firstname) size)
  }
  
  @Test
  def Select2 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name).size)
  }
  
  @Test
  def Select2_2 {
    assertEquals(2, survey.select(_.id, _.name) size)
  }
  
  @Test
  def Select3 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name, survey.name.substring(0,1)).size)
  }
  
  @Test
  def Select3_2 {
    assertEquals(2, survey.select(_.id, _.name, _.name.substring(0,1)) size)
  }
  
  @Test
  def Select4 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name, survey.name + "X", survey.name + "Y").size)
  }
  
  @Test
  def Select4_2 {
    assertEquals(2, survey.select(_.id, _.name, _.name + "X", _.name + "Y") size)
  }
  
  @Test
  def Count {
    assertEquals(2, query.from(survey).count)
    assertEquals(2, query.from(employee).count)
  }
  
  @Test
  def Count_2 {
    assertEquals(2, survey.query.count)
    assertEquals(2, employee.query.count)
  }
  
  @Test
  def Order_By {
    query.from(survey).orderBy(survey.id asc).select(survey)
  }
  
  @Test
  def Order_By_2 {
    survey.orderBy(_.id asc).select
  }
  
  @Test
  def Join {
    val sup = Employee as "sup"
    val result: List[(Employee, Employee)] = employee.join(_.superiorFk, sup).select
  }
  
  @Test
  def Unique_Result {
    assertEquals("abc", query.from(survey).where(survey.id eq 1).uniqueResult(survey.name))
    assertEquals("def", query.from(survey).where(survey.id eq 2).uniqueResult(survey.name))
    assertEquals("Bob", query.from(employee).where(employee.lastname eq "Smith").uniqueResult(employee.firstname))
    assertEquals("John", query.from(employee).where(employee.lastname eq "Doe").uniqueResult(employee.firstname))
  }  
  
  @Test
  def Unique {
    assertEquals("abc", survey.where(_.id eq 1).unique(_.name).get)
    assertEquals("def", survey.where(_.id eq 2).unique(_.name).get)
    assertEquals("Bob", employee.where(_.lastname eq "Smith").unique(_.firstname).get)
    assertEquals("John", employee.where(_.lastname eq "Doe").unique(_.firstname).get)
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

  def query = new SQLQuery(connection, templates)

  def delete(path: RelationalPath[_]) = new SQLDeleteClause(connection, templates, path)
  
  def insert(path: RelationalPath[_]) = new SQLInsertClause(connection, templates, path)
  
  def update(path: RelationalPath[_]) = new SQLUpdateClause(connection, templates, path)
  
}
