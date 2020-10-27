package com.querydsl.scala.sql

import java.sql.{Connection, DriverManager, Statement}

import com.querydsl.sql.dml._
import com.querydsl.sql.{Configuration, HSQLDBTemplates, RelationalPath, SQLQuery}
import org.junit.Assert._
import org.junit._
import test._

object JDBCIntegrationTest {

  private var connection: Connection = _
  private var statement: Statement = _

  @BeforeClass
  def setUpClass() {
    Class.forName("org.h2.Driver")
    val url = "jdbc:h2:mem:testdb" + System.currentTimeMillis()

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

  @AfterClass
  def tearDownClass() {
    try {
      statement.close()
    } finally {
      connection.close()
    }
  }

}

class JDBCIntegrationTest extends SQLHelpers {

  val survey = QSurvey
  val employee = QEmployee

  val templates = new HSQLDBTemplates()
  val configuration = new Configuration(templates)

  def connection = JDBCIntegrationTest.connection

  @Before
  def setUp(): Unit = {
    connection.setAutoCommit(false)
  }

  @After
  def tearDown(): Unit = {
    connection.rollback()
    connection.setAutoCommit(true)
  }

  @Test
  def Populate_Bean {
    assertEquals(2, query.from(survey).select(survey).fetch() size ())
  }

  @Test
  def List {
    assertEquals(2, query.from(survey).select(survey.id).fetch() size ())
    assertEquals(2, query.from(employee).select(employee.firstname).fetch() size ())
  }

  @Test
  def Select2 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name).fetch().size)
  }

  @Test
  def Select3 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name, survey.name.substring(0,1)).fetch().size)
  }

  @Test
  def Select4 {
    assertEquals(2, query.from(survey).select(survey.id, survey.name, survey.name + "X", survey.name + "Y").fetch().size)
  }

  @Test
  def Count {
    assertEquals(2, query.from(survey).fetchCount)
    assertEquals(2, query.from(employee).fetchCount)
  }

  @Test
  def Order_By {
    query.from(survey).orderBy(survey.id asc).select(survey)
  }

  @Test
  def Unique_Result {
    assertEquals("abc", query.from(survey).where(survey.id eq 1).select(survey.name).fetchOne() orElse null)
    assertEquals("def", query.from(survey).where(survey.id eq 2).select(survey.name).fetchOne() orElse null)
    assertEquals("Bob", query.from(employee).where(employee.lastname eq "Smith").select(employee.firstname).fetchOne() orElse null)
    assertEquals("John", query.from(employee).where(employee.lastname eq "Doe").select(employee.firstname).fetchOne() orElse null)
  }

  @Test
  def Insert {
    val s = new Survey()
    s.name = "XXX"

    val id = insert(survey) populate(s) executeWithKey(survey.id)
    val sNew = query from survey where (survey.id === id) select (survey) fetchOne() orElse null
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

    val sNew = query from survey where (survey.id === id) select (survey) fetchOne() orElse null
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

  def query = new SQLQuery[Void](connection, configuration)

  def delete(path: RelationalPath[_]) = new SQLDeleteClause(connection, configuration, path)

  def insert(path: RelationalPath[_]) = new SQLInsertClause(connection, configuration, path)

  def update(path: RelationalPath[_]) = new SQLUpdateClause(connection, configuration, path)

}
