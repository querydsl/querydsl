package com.querydsl.scala.sql

import org.junit._
import org.junit.Assert._
import com.querydsl.sql._
import com.querydsl.core.types._
import test._

class PathsTest {
  
  @Test
  def Projection {
    val projection = Employee.getProjection.asInstanceOf[FactoryExpression[_]]
    assertEquals(4, projection.getArgs.size)
  }
  
}  