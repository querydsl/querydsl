package com.mysema.query.scala.sql

import org.junit._
import org.junit.Assert._
import com.mysema.query.sql._
import com.mysema.query.types._
import test._

class PathsTest {
  
  @Test
  def Projection {
    val projection = Employee.getProjection.asInstanceOf[FactoryExpression[_]]
    assertEquals(4, projection.getArgs.size)
  }
  
}  