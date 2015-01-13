package com.querydsl.scala.sql

import org.junit._
import org.junit.Assert._
import com.querydsl.sql._
import com.querydsl.core.types._
import java.sql.Connection

import test._

class QueriesTest extends SQLHelpers {
  
  val templates = new H2Templates()
  
  def connection: Connection = null
    
  @Test
  def From {
    assertEquals("from EMPLOYEE employee", Employee.query.toString)
  }
  
  @Test
  def From_Where {
    assertEquals("from EMPLOYEE employee\nwhere employee.FIRSTNAME = ?", 
        Employee.where(_.firstname eq "XXX").toString)
  }
  
  @Test
  def From_Where_Limit {
    assertEquals("from EMPLOYEE employee\nwhere employee.FIRSTNAME = ?\nlimit ?", 
        Employee.where(_.firstname eq "XXX").limit(1).toString)
  }
  
  @Test
  def From_Join {        
    val sup = Employee as "sup"
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID = sup.ID", 
        Employee.join(_.superiorFk, sup).toString)
  }
  
  @Test
  def From_Join_2x {
    val sup = Employee as "sup"
    val sup2 = Employee as "sup2"
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID " +
        "= sup.ID\ninner join EMPLOYEE sup2\non sup.SUPERIOR_ID = sup2.ID", 
        Employee.join(_.superiorFk, sup).join(sup.superiorFk, sup2).toString)
  }
  
  @Test
  def From_Join_Where {  
    val sup = Employee as "sup"
    val sup2 = Employee as "sup2"
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID " +
        "= sup.ID\nwhere employee.ID = ?", 
        Employee.join(_.superiorFk, sup).where( _.id eq 1).toString)
  }
  
  @Test
  def From_Join_Where2 {  
    val sup = Employee as "sup"
    val sup2 = Employee as "sup2"
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID " +
        "= sup.ID\nwhere employee.ID = sup.ID", 
        Employee.join(_.superiorFk, sup).where(_.id eq sup.id).toString)
  }
  
}