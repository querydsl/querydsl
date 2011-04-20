package com.mysema.query.scala;

//import com.mysema.query._
import com.mysema.query.types._
import com.mysema.query.scala.Queries._
import com.mysema.query.scala.Conversions._

import com.mysema.query.jpa.impl.JPAQuery
import com.mysema.query.scala.Conversions._

import javax.persistence._;
import org.junit.{ Test, Before, After };
import org.junit.Assert._

class QueriesTest {
    
  var entityManager: EntityManager = _;
  val user = alias(classOf[User])
  val department = alias(classOf[Department])
  val company = alias(classOf[Company])

  @Before 
  def before {
    var entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb")
    entityManager = entityManagerFactory.createEntityManager()
  }  
   
  @Test 
  def Unique {
    query from(user) unique(user) getOrElse(new User());  
  }
  
  @Test 
  def Single {
    query from(user) single(user) getOrElse(new User());  
  }
  
  def query() = new JPAQuery(entityManager)
  
}