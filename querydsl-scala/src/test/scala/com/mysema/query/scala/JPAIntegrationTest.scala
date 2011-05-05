package com.mysema.query.scala

import com.mysema.query.jpa.impl.JPAQuery
import com.mysema.query.scala.Conversions._

import javax.persistence._;
import org.junit.{ Test, Before, After };
import org.junit.Assert._

import scala.reflect.BeanProperty;
import scala.annotation.target._;

class JPAIntegrationTest {

  var entityManager: EntityManager = _;

  @Before
  def setUp() {
    var entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb")
    entityManager = entityManagerFactory.createEntityManager()

    val company = new Company(1, "Example", null)

    val department1 = new Department()
    department1.id = 2
    department1.name = "HR"
    department1.company = company
    val department2 = new Department()
    department2.id = 3
    department2.name = "Sales"
    department2.company = company

    val user1 = new User()
    val user2 = new User()
    user1.id = 4
    user2.id = 5
    user1.userName = "Bob"
    user2.userName = "Ann"
    user1.department = department1
    user2.department = department2

    entityManager.getTransaction().begin()

    List(company, department1, department2, user1, user2).foreach({ entityManager.persist(_) })
    entityManager.flush()
  }

  @After
  def tearDown() {
    if (entityManager != null) {
      entityManager.getTransaction().rollback()
      entityManager.close()
    }
  }

  @Test
  def test() {
    val user = alias(classOf[User])
    val department = alias(classOf[Department])
    val company = alias(classOf[Company])

    assertEquals(2, query from user count)
    assertEquals(2, query from department count)
    assertEquals(1, query from company count)

    entityManager.flush()

    assertEquals(2, query from user list user size)
    assertEquals(2, query from department list department size)
    assertEquals(1, query from company list company size)

    assertEquals("Bob", query from user where (user.userName $eq "Bob") uniqueResult user.userName)
    assertEquals("Bob", query from user where (user.userName $like "Bo%") uniqueResult user.userName)
    assertEquals("Ann", query from user where (user.department.name $eq "Sales") uniqueResult user.userName)
  }

  def query() = new JPAQuery(entityManager)

}

@Entity
class User {
  @Id
  @BeanProperty var id: Integer = _
  
  @BeanProperty var userName: String = _
  
  @ManyToOne
  @BeanProperty var department: Department = _
}

@Entity
class Department {
  
  @Id
  @BeanProperty var id: Integer = _
  
  @BeanProperty var name: String = _
  
  @ManyToOne
  @BeanProperty var company: Company = _
}

@Entity
class Company {
  
  @Id
  @BeanProperty var id: Integer = _
  
  @BeanProperty var name: String = _
  
  @OneToMany(mappedBy = "company")
  @BeanProperty var departments: java.util.Set[Department] = _

  def this(id: Integer,
           name: String,
           departments: java.util.Set[Department]) {
    this()
    this.id = id
    this.name = name
    this.departments = departments
  }

}
