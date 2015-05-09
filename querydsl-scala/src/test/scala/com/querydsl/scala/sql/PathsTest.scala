package com.querydsl.scala.sql

import com.querydsl.core.types._
import org.junit.Assert._
import org.junit._
import test._

class PathsTest {
  
  @Test
  def Projection {
    val projection = Employee.getProjection.asInstanceOf[FactoryExpression[_]]
    assertEquals(4, projection.getArgs.size)
  }
  
}  