package com.querydsl.scala;

import com.querydsl.core.types._;

import org.junit.{ Test, Before, After, Assert };

import Matchers._

class ExpressionTest {

  val person = Person as "person"
  
  def assertEquals(expected: String, actual: Any) {
    Assert.assertEquals(expected, actual.toString)
  }  
  
  @Test
  def Double_Negation {
    assertEquals("person.javaInt", person.javaInt.negate.negate);  
  }
    
  @Test
  def Is_Not_Null {
    assertEquals("person.other is not null", person.other isNotNull)
    assertEquals("person.other is not null", person.other is not(null) )   
  }
  
  @Test
  def Is_Null {
    assertEquals("person.other is null", person.other isNull)  
    assertEquals("person.other is null", person.other is null )  
  }
  
  @Test
  def Long_Path {
    assertEquals("person.other.firstName = Ben", person.other.firstName eq "Ben")
  }
  
  @Test
  def Long_Path_With_Operators {
    assertEquals("person.other.firstName = Ben", person.other.firstName === "Ben")
  }
  
  @Test
  def Path_Equality {
    assertEquals("person.firstName = person.lastName", person.firstName eq person.lastName)
  }
  
  @Test
  def Path_Equality_With_Operators {
    assertEquals("person.firstName = person.lastName", person.firstName === person.lastName)
  }

  @Test
  def String_Equality {
    assertEquals("person.firstName = Ben", person.firstName eq "Ben")
    assertEquals("person.firstName != Ben", person.firstName ne "Ben")
    assertEquals("person.firstName != Ben", person.firstName ne "Ben")
  }
  
  @Test
  def String_Equality_With_Operators {
    assertEquals("person.firstName = Ben", person.firstName === "Ben")
    assertEquals("person.firstName != Ben", person.firstName !== "Ben")
    assertEquals("person.firstName != Ben", person.firstName !== "Ben")
  }
    
  @Test
  def String_Like {
    assertEquals("person.firstName like Ben", person.firstName like "Ben")
  }

  @Test
  def String_Order {
    assertEquals("person.firstName ASC", person.firstName asc)
  }

  @Test
  def String_Append {
    assertEquals("person.firstName + x", person.firstName append "x")
  }
  
  @Test
  def String_Append_With_Operators {
    assertEquals("person.firstName + x", person.firstName + "x")
  }
  
  @Test
  def String_Append2 {
    assertEquals("person.firstName +   + person.lastName", person.firstName append " " append person.lastName)
  }
  
  @Test
  def String_Append2_With_Operators {
    assertEquals("person.firstName +   + person.lastName", person.firstName + " " + person.lastName)
  }

  @Test
  def String_And {
    val andClause = (person.firstName like "An%") and (person.firstName like "Be%")
    assertEquals("person.firstName like An% && person.firstName like Be%", andClause)
  }
  
  @Test
  def String_And_With_Operators {
    val andClause = (person.firstName like "An%") && (person.firstName like "Be%")
    assertEquals("person.firstName like An% && person.firstName like Be%", andClause)
  }

  @Test
  def String_Or {
    val orClause = (person.firstName like "An%") or (person.firstName like "Be%")
    assertEquals("person.firstName like An% || person.firstName like Be%", orClause)
  }

  @Test
  def String_Or_With_Operators {
    val orClause = (person.firstName like "An%") || (person.firstName like "Be%")
    assertEquals("person.firstName like An% || person.firstName like Be%", orClause)
  }
  
  @Test
  def String_Not {
    assertEquals("!(person.firstName like An%)", (person.firstName like "An%") not)
  }

  @Test
  def String_Not_With_Operators {
    assertEquals("!(person.firstName like An%)", !(person.firstName like "An%"))
  }
    
  @Test
  def String_Trim {
    assertEquals("trim(person.firstName)", person.firstName trim)
  }

  @Test
  def String_Is_Empty {
    assertEquals("empty(person.firstName)", person.firstName isEmpty)
    assertEquals("empty(person.firstName)", person.firstName is empty)
  }
  
  @Test
  def String_Is_Not_Empty {
    assertEquals("!empty(person.firstName)", person.firstName isNotEmpty)
    assertEquals("!empty(person.firstName)", person.firstName not empty)
//    assertEquals("!empty(person.firstName)", person.firstName is not(empty) )
  }

  @Test
  def Number_Comparison {
    assertEquals("person.scalaInt < 5", person.scalaInt lt 5)
    assertEquals("person.scalaInt = 5", person.scalaInt eq 5)
    
    assertEquals("person.javaInt < 5", person.javaInt lt 5)
    assertEquals("person.javaInt > 5", person.javaInt gt 5)
    assertEquals("person.javaInt <= 5", person.javaInt loe 5)
    assertEquals("person.javaInt >= 5", person.javaInt goe 5)
    assertEquals("person.javaInt = 5", person.javaInt eq 5.asInstanceOf[Integer]) // FIXME
    assertEquals("person.javaInt != 5", person.javaInt ne 5.asInstanceOf[Integer]) // FIXME
  }
  
  @Test
  def Number_Negation {
    assertEquals("-person.scalaInt", -person.scalaInt)  
  }
  
  @Test
  def Number_Comparison_With_Operators {
    assertEquals("person.scalaInt < 5", person.scalaInt < 5)
    assertEquals("person.scalaInt = 5", person.scalaInt === 5) 
    
    assertEquals("person.javaInt < 5", person.javaInt < 5)
    assertEquals("person.javaInt > 5", person.javaInt > 5)
    assertEquals("person.javaInt <= 5", person.javaInt <= 5)
    assertEquals("person.javaInt >= 5", person.javaInt >= 5)
    assertEquals("person.javaInt = 5", person.javaInt === 5)
    assertEquals("person.javaInt != 5", person.javaInt !== 5)
  }
    
  @Test
  def Number_Between {
    assertEquals("person.scalaInt between 2 and 3", person.scalaInt between (2, 3))
    assertEquals("person.javaInt between 2 and 3", person.javaInt between (2, 3))
  }
  
  @Test
  def Number_Not_Between {
    assertEquals("!(person.scalaInt between 2 and 3)", person.scalaInt not between (2, 3))
    assertEquals("!(person.javaInt between 2 and 3)", person.javaInt not between (2, 3))
  }

  @Test
  def Number_Arithmetic {
    assertEquals("person.scalaInt + 3", person.scalaInt add 3)
    assertEquals("person.scalaInt - 3", person.scalaInt subtract 3)
    assertEquals("person.scalaInt / 3", person.scalaInt divide 3)
    assertEquals("person.scalaInt * 3", person.scalaInt multiply 3)
    assertEquals("-person.scalaInt", person.scalaInt negate)
    assertEquals("person.scalaInt % 4", person.scalaInt mod 4)
    assertEquals("round(person.scalaInt)", person.scalaInt round)
    assertEquals("floor(person.scalaInt)", person.scalaInt floor)
    assertEquals("ceil(person.scalaInt)", person.scalaInt ceil)
    assertEquals("sqrt(person.scalaInt)", person.scalaInt sqrt)
  }

  @Test
  def Number_Arithmetic_With_Operators {
    assertEquals("person.scalaInt + 3", person.scalaInt + 3)
    assertEquals("person.scalaInt - 3", person.scalaInt - 3)
    assertEquals("person.scalaInt / 3", person.scalaInt / 3)
    assertEquals("person.scalaInt * 3", person.scalaInt * 3)
    assertEquals("-person.scalaInt", -person.scalaInt)
    assertEquals("person.scalaInt % 4", person.scalaInt % 4)
  }  
  
  @Test
  def Number_Casts {
    assertEquals("cast(person.javaInt,long)", person.javaInt longValue)
    assertEquals("cast(person.scalaInt,long)", person.scalaInt longValue)
  }

  @Test
  def Java_Collections_Size {
    assertEquals("size(person.javaCollection)", person.javaCollection size)
    assertEquals("size(person.javaSet)", person.javaSet size)
    assertEquals("size(person.javaList)", person.javaList size)
    assertEquals("size(person.javaMap)", person.javaMap size)
  }

  @Test
  def Java_Collections_Is_Empty {
    assertEquals("empty(person.javaCollection)", person.javaCollection isEmpty)
    assertEquals("empty(person.javaSet)", person.javaSet isEmpty)
    assertEquals("empty(person.javaList)", person.javaList isEmpty)
    assertEquals("empty(person.javaMap)", person.javaMap isEmpty)
  }

  @Test
  def Java_Collections_Get {
    assertEquals("person.javaList.get(0) is not null", person.javaList.get(0) isNotNull)
    assertEquals("person.javaMap.get(xxx) is null", person.javaMap.get("xxx") isNull)
  }
  
  @Test
  def Java_Collection_Get_With_Apply {
    assertEquals("person.javaList.get(0) is not null", person.javaList(0) isNotNull)
  }

  @Test
  def Java_Collections_Get_And_Starts_With {
    assertEquals("startsWith(person.javaMap.get(xxx),X)", person.javaMap.get("xxx") startsWith "X")
  }
  
  @Test
  def Java_Collections_Get_With_Apply {
    assertEquals("startsWith(person.javaMap.get(xxx),X)", person.javaMap("xxx") startsWith "X")
  }  

  @Test
  def Scala_Collections_Size {
    assertEquals("size(person.scalaList)", person.scalaList size)
    assertEquals("size(person.scalaMap)", person.scalaMap size)
  }

  @Test
  def Scala_Collections_Is_Empty {
    assertEquals("empty(person.scalaList)", person.scalaList isEmpty)
    assertEquals("empty(person.scalaMap)", person.scalaMap isEmpty)
  }

  @Test
  def Scala_Collections_Get {
    assertEquals("person.scalaList.get(0) is not null", person.scalaList.get(0) isNotNull)
    assertEquals("person.scalaList.get(0) is not null", person.scalaList.get(0) isNotNull)
    assertEquals("person.scalaMap.get(xxx) is null", person.scalaMap.get("xxx") isNull)
  }

  @Test
  def Scala_Collections_Contains {
    assertEquals("X in person.scalaList", person.scalaList contains "X")
    assertEquals("X in person.javaList", person.javaList contains "X")
  }

  @Test
  def Scala_Collections_Get_And_Starts_With {
    assertEquals("startsWith(person.scalaMap.get(xxx),X)", person.scalaMap.get("xxx") startsWith "X")
  }

  @Test
  def Array_Size {   
    assertEquals("size(person.array)", person.array size)
  }

  @Test
  def Complex_Starts_With_And_Less_Than_And_Is_Null {
    val expr: Predicate = (person.firstName startsWith person.lastName) and (person.javaInt lt person.scalaInt) or (person.javaDouble isNull)
    assertEquals("startsWith(person.firstName,person.lastName) " +
         "&& person.javaInt < person.scalaInt " +
         "|| person.javaDouble is null", expr)
  }
  
  @Test
  def Complex_Starts_With_And_Less_Than_And_Is_Null_With_Operators {
    val expr: Predicate = (person.firstName startsWith person.lastName) and (person.javaInt lt person.scalaInt) or (person.javaDouble isNull)
    assertEquals("startsWith(person.firstName,person.lastName) && person.javaInt < person.scalaInt || person.javaDouble is null", 
                  person.firstName.startsWith(person.lastName) && person.javaInt < person.scalaInt || person.javaDouble.isNull)
  }
  
  @Test
  def Lt_and_Gt {
    val expr: Predicate = (person.javaInt lt 5) and (person.scalaInt gt 7)
    assertEquals("person.javaInt < 5 && person.scalaInt > 7", expr)
  }
  
  @Test
  def Lt_and_Gt_With_Operators {
    assertEquals("person.javaInt < 5 && person.scalaInt > 7", person.javaInt < 5 && person.scalaInt > 7)
  }
  
  @Test
  def Gt_and_Lt {
    val expr: Predicate = (person.javaInt gt 5) and (person.scalaInt lt 7)
    assertEquals("person.javaInt > 5 && person.scalaInt < 7", expr)
  }
  
  @Test
  def Gt_and_Lt_With_Operators {
    assertEquals("person.javaInt > 5 && person.scalaInt < 7", person.javaInt > 5 && person.scalaInt < 7)
  }
  
  @Test
  def Lt_or_Gt {
    val expr: Predicate = (person.javaInt lt 5) or (person.scalaInt gt 7)
    assertEquals("person.javaInt < 5 || person.scalaInt > 7", expr)
  }
  
  @Test
  def Lt_or_Gt_With_Operators {
    assertEquals("person.javaInt < 5 || person.scalaInt > 7", person.javaInt < 5 || person.scalaInt > 7)
  }  
  
  @Test
  def Gt_or_Lt {
    val expr: Predicate = (person.javaInt gt 5) or (person.scalaInt lt 7)
    assertEquals("person.javaInt > 5 || person.scalaInt < 7", expr)
  }

  @Test
  def Gt_or_Lt_With_Operators {
    assertEquals("person.javaInt > 5 || person.scalaInt < 7", person.javaInt > 5 || person.scalaInt < 7)
  }
  
  @Test
  def Starts_with {
    assertEquals("startsWith(person.firstName,amin)",  person.firstName startsWith "amin")
  }
  
  @Test
  def Ends_with {
    assertEquals("endsWith(person.firstName,amin)",  person.firstName endsWith "amin")
  }
  
  @Test
  def String_Negations {
    assertEquals("!(person.firstName like XXX)", person.firstName not like("XXX"))
    assertEquals("!matches(person.firstName,XXX)", person.firstName not matches("XXX"))
    assertEquals("!startsWith(person.firstName,XXX)", person.firstName not startsWith("XXX"))   
    assertEquals("!endsWith(person.firstName,XXX)", person.firstName not endsWith("XXX")) 
    assertEquals("!empty(person.firstName)", person.firstName not empty) 
  }
  
  @Test
  def String_Negations2 {
    assertEquals("!(person.firstName like XXX)", !(person.firstName like "XXX"))
    assertEquals("!matches(person.firstName,XXX)", !(person.firstName matches "XXX")) 
    assertEquals("!startsWith(person.firstName,XXX)", !(person.firstName startsWith "XXX"))   
    assertEquals("!endsWith(person.firstName,XXX)", !(person.firstName endsWith "XXX")) 
    assertEquals("!empty(person.firstName)", !(person.firstName isEmpty)) 
  }

  @Test
  def BooleanExpression_All_Of {
    val b1 = new BooleanPath("b1")
    val b2 = new BooleanPath("b2")
    val b3 = new BooleanPath("b3")
    assertEquals("b1 && b2 && b3", BooleanExpression.allOf(b1, b2, b3))       
    assertEquals("b1", BooleanExpression.allOf(b1))
  }

  @Test
  def BooleanExpression_Any_Of {
    val b1 = new BooleanPath("b1")
    val b2 = new BooleanPath("b2")
    val b3 = new BooleanPath("b3")
    assertEquals("b1 || b2 || b3", BooleanExpression.anyOf(b1, b2, b3))
    assertEquals("b1", BooleanExpression.anyOf(b1))
  }
  
}


