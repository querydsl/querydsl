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
    assertEquals(Set(classOf[java.io.Serializable],classOf[Comparable[_]],classOf[CharSequence]), 
        ReflectionUtils.getImplementedInterfaces(classOf[String]))
  }
  
  @Test
  def getFields {
    assertTrue( ReflectionUtils.getFields(classOf[String]).size > 0)
  }
  
  
  
}