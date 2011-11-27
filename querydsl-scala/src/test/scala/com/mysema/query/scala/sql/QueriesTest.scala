package com.mysema.query.scala.sql

import org.junit._
import org.junit.Assert._
import com.mysema.query.sql._


import test._

class QueriesTest {
  
  import RichSimpleQuery._  
  
  val sup = QEmployee as "sup"
  
  val sup2 = QEmployee as "sup2"
  
  private val templates = new H2Templates()
    
  implicit def toRichSimpleQuery[T, R <: RelationalPath[T]](p: RelationalPath[T] with R) = {
    new RichSimpleQuery[T, R](p, new SQLQueryImpl(null, templates).from(p) )
  }  
  
  @Test
  def From {
    assertEquals("from EMPLOYEE employee", QEmployee.query.toString)
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
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID = sup.ID", 
        Employee.join(_.superiorFk, sup).toString)
  }
  
  @Test
  def From_Join_2x {
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID = sup.ID\ninner join EMPLOYEE sup2\non sup.SUPERIOR_ID = sup2.ID", 
        Employee.join(_.superiorFk, sup).join(_._2.superiorFk, sup2).toString)
  }
  
  @Test
  def From_Join_Where {  
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID = sup.ID\nwhere employee.ID = ?", 
        Employee.join(_.superiorFk, sup).where( _._1.id eq 1).toString)
  }
  
  @Test
  def From_Join_Where2 {  
    assertEquals(
        "from EMPLOYEE employee\ninner join EMPLOYEE sup\non employee.SUPERIOR_ID = sup.ID\nwhere employee.ID = sup.ID", 
        Employee.join(_.superiorFk, sup).where(e => e._1.id eq e._2.id).toString)
  }
  
}