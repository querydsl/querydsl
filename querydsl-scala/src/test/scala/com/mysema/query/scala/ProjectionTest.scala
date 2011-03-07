package com.mysema.query.scala

import com.mysema.query.scala.Conversions._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.types._
import com.mysema.query.types.path._

import java.util.Arrays._

import org.junit.Test
import org.junit.Assert._

class ProjectionTest {
    
  val person = alias(classOf[Person])

  @Test
  def QBean(){
    //val bean = new QBean(classOf[Person], person.firstName, person.lastName)        
  }
    
  @Test
  def QTuple(){
    val tuple = new QTuple(person.firstName, person.lastName)
    assertEquals(asList(person.firstName.~, person.lastName.~), tuple.getArgs)
  }
    
}