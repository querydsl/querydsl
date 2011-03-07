package com.mysema.query.scala.sql

import com.mysema.query.sql._;
import com.mysema.query.types.path._;
import java.io.File

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit._
import org.junit.Assert._

import java.util.Arrays;

import com.mysema.query.scala.ScalaTypeMappings

class JDBCIntegrationTest {

  val templates = new HSQLDBTemplates();

  var connection: Connection = _;

  var statement: Statement = _;

  @Before
  def setUp() {
    Class.forName("org.hsqldb.jdbcDriver");
    val url = "jdbc:hsqldb:mem:testdb";
    connection = DriverManager.getConnection(url, "sa", "");
    statement = connection.createStatement();
    statement.execute("drop table employee if exists");
    statement.execute("drop table survey if exists");
    statement.execute("drop table date_test if exists");
    statement.execute("drop table date_time_test if exists");

    statement.execute("create table survey ("
      + "id int, "
      + "name varchar(30), "
      + "CONSTRAINT PK_survey PRIMARY KEY (id, name))");

    statement.execute("insert into survey values (1, 'abc')");
    statement.execute("insert into survey values (2, 'def')");

    statement.execute("create table employee("
      + "id INT, "
      + "firstname VARCHAR(50), "
      + "lastname VARCHAR(50), "
      + "superior_id int, "
      + "CONSTRAINT PK_employee PRIMARY KEY (id), "
      + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");

    statement.execute("insert into employee values (1, 'Bob', 'Smith', null)");
    statement.execute("insert into employee values (2, 'John', 'Doe', null)");
  }

  @Test
  def Generation_without_Beantypes() {
    val namingStrategy = new DefaultNamingStrategy();
    val exporter = new MetaDataExporter();
    exporter.setNamePrefix("Q");
    exporter.setPackageName("test");
    exporter.setTargetFolder(new File("target/gen1"));
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
    exporter.setCreateScalaSources(true);
    exporter.setTypeMappings(ScalaTypeMappings.create);
    exporter.export(connection.getMetaData);
  }

  @Test
  def Generation_with_Beantypes() {
    val namingStrategy = new DefaultNamingStrategy();
    val beanSerializer = new ScalaBeanSerializer();
    val exporter = new MetaDataExporter();
    exporter.setNamePrefix("Q");
    exporter.setPackageName("test");
    exporter.setTargetFolder(new File("target/gen2"));
    exporter.setSerializerClass(classOf[ScalaMetaDataSerializer]);
    exporter.setBeanSerializer(beanSerializer)
    exporter.setCreateScalaSources(true);
    exporter.setTypeMappings(ScalaTypeMappings.create);
    exporter.export(connection.getMetaData);
  }

  @Test
  def Querying() {
    val survey = new QSurvey("survey");
    val employee = new QEmployee("employee");

    // list
    assertEquals(2, query from (survey) list (survey.id) size ());
    assertEquals(2, query from (employee) list (employee.firstname) size ());

    // count
    assertEquals(2, query from (survey) count);
    assertEquals(2, query from (employee) count);

    // uniqueResult
    assertEquals("abc", query from survey where (survey.id eq 1) uniqueResult survey.name);
    assertEquals("def", query from survey where (survey.id eq 2) uniqueResult survey.name);
    assertEquals("Bob", query from employee where (employee.lastname eq "Smith") uniqueResult employee.firstname);
    assertEquals("John", query from employee where (employee.lastname eq "Doe") uniqueResult employee.firstname);
  }

  @After
  def tearDown() {
    try {
      statement.close();
    } finally {
      connection.close();
    }
  }

  def query = new SQLQueryImpl(connection, templates);

}

@Table("survey")
class QSurvey(path: String) extends RelationalPathBase[QSurvey](classOf[QSurvey], path) {
  val id: NumberPath[Integer] = createNumber("id", classOf[Integer]);

  val name: StringPath = createString("name");

  val sysIdx46: PrimaryKey[QSurvey] = createPrimaryKey(id, name);

  val _surveyFk: ForeignKey[QEmployee] = createInvForeignKey(Arrays.asList(id, id), Arrays.asList("survey_id", "survey_id"));

}

@Table("employee")
class QEmployee(path: String) extends RelationalPathBase[QEmployee](classOf[QEmployee], path) {
  val firstname: StringPath = createString("firstname");

  val id: NumberPath[Integer] = createNumber("id", classOf[Integer]);

  val lastname: StringPath = createString("lastname");

  val superiorId: NumberPath[Integer] = createNumber("superior_id", classOf[Integer]);

  val sysIdx47: PrimaryKey[QEmployee] = createPrimaryKey(id);

  val superiorFk: ForeignKey[QEmployee] = createForeignKey(superiorId, "id");

  val _superiorFk: ForeignKey[QEmployee] = createInvForeignKey(id, "superior_id");

}