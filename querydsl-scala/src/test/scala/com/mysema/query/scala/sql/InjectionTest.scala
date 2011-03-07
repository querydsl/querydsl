package com.mysema.query.scala.sql

import com.mysema.query.sql._
import com.mysema.query.types.Predicate
import com.mysema.query.types.template.BooleanTemplate

import org.junit.Test
import org.junit.Assert._

class InjectionTest {
  
  implicit def asPredicate(str: String): Predicate = BooleanTemplate.create(str)
  
  @Test
  def Injection() {
    val c = QCategory as "c"
    val sq = query from c where "c.name like \"a%\"" list c
    
    assertEquals("c.name like \"a%\"", sq.getMetadata.getWhere.toString)
  }
  
  def query() = new SQLSubQuery()
  
  

}