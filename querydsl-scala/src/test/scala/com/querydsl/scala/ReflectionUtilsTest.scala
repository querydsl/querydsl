package com.querydsl.scala

import org.junit.Assert._
import org.junit._

class ReflectionUtilsTest {

  @Test
  def getSuperClasses_of_String {
    assertEquals(List(classOf[String],classOf[AnyRef]), ReflectionUtils.getSuperClasses(classOf[String]))
  }

  @Test
  def getImplementedInterfaces {
    var setToTest = ReflectionUtils.getImplementedInterfaces(classOf[String])
    assertTrue(setToTest.contains(classOf[java.io.Serializable]))
    assertTrue(setToTest.contains(classOf[Comparable[_]]))
    assertTrue(setToTest.contains(classOf[CharSequence]))
  }

  @Test
  def getFields {
    assertTrue( ReflectionUtils.getFields(classOf[String]).size > 0)
  }



}