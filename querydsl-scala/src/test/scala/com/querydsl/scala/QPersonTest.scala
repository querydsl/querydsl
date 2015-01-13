package com.querydsl.scala;

import com.querydsl.core.types._;

import org.junit.{ Test, Before, After, Assert };

import Matchers._

class QPersonTest {

  val person = QPerson as "person"
      
  @Test 
  def EntityPath {
    assertEquals("person.other.firstName", person.other.firstName)
  }
  
  @Test 
  def Collection_Any {  
    assertEquals("any(person.javaCollection) = Bob", person.javaCollection.any === "Bob")
  }
  
  @Test 
  def List_Get {
    assertEquals("person.javaList.get(0) = Bob", person.javaList(0) === "Bob")
  }
  
  @Test
  def List_Get_EntityPath {
    assertEquals("person.listOfPersons.get(0).firstName is not null", 
        person.listOfPersons(0).firstName isNotNull)
  }
    
  def assertEquals(expected: String, actual: Any) {
    Assert.assertEquals(expected, actual.toString)
  }  

}