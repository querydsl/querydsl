package com.mysema.query.scala;

import com.mysema.query.scala.Conversions._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.types._
import com.mysema.query.types.path._
import org.junit.Test
import org.junit.Assert

class AliasTest {

  val person = alias(classOf[Person])

  def assertEquals(expected: String, actual: Expression[_]) {
    Assert.assertEquals(expected, actual.toString);
  }  
  
  def assertEquals(expected: String, actual: OrderSpecifier[_]) {
    Assert.assertEquals(expected, actual.toString);
  }

  @Test
  def Path_Equality {
    assertEquals("person.firstName = person.lastName", person.firstName $eq person.lastName);
  }

  @Test
  def String_Equality {
    assertEquals("person.firstName = Ben", person.firstName $eq "Ben");
    assertEquals("person.firstName != Ben", person.firstName $ne "Ben");
    assertEquals("person.firstName != Ben", person.firstName $ne "Ben");
  }
  
  @Test
  def String_Equality2 {
    val predicate: Predicate = person.firstName is "Ben";
    assertEquals("person.firstName = Ben", predicate);
  }
    
  @Test
  def String_Like {
    assertEquals("person.firstName like Ben", person.firstName like "Ben");
  }

  @Test
  def String_Order {
    assertEquals("person.firstName ASC", person.firstName asc);
  }

  @Test
  def String_Append {
    assertEquals("person.firstName + x", person.firstName append "x");
  }
  
  @Test
  def String_Append2 {
    assertEquals("person.firstName +   + person.lastName", person.firstName append " " append person.lastName);
  }

  @Test
  def String_And {
    val andClause = (person.firstName like "An%") and (person.firstName like "Be%");
    assertEquals("person.firstName like An% && person.firstName like Be%", andClause);
  }

  @Test
  def String_Or {
    val orClause = (person.firstName like "An%") or (person.firstName like "Be%");
    assertEquals("person.firstName like An% || person.firstName like Be%", orClause);
  }

  @Test
  def String_Not {
    assertEquals("!person.firstName like An%", (person.firstName like "An%") not);
    assertEquals("!person.firstName like An%", not(person.firstName like "An%"));
  }

  @Test
  def String_Trim {
    assertEquals("trim(person.firstName)", person.firstName trim);
  }

  @Test
  def String_Is_Empty {
    val predicate: Predicate = person.firstName isEmpty();
    assertEquals("empty(person.firstName)", predicate);
  }

  @Test
  def Number_Comparison {
    val predicate: Predicate = person.scalaInt lt 5;
    assertEquals("person.scalaInt < 5", predicate);
    assertEquals("person.javaInt < 5", person.javaInt lt 5);
    assertEquals("person.javaInt > 5", person.javaInt gt 5);
    assertEquals("person.javaInt <= 5", person.javaInt loe 5);
    assertEquals("person.javaInt >= 5", person.javaInt goe 5);
    assertEquals("person.javaInt = 5", person.javaInt $eq 5);
    assertEquals("person.javaInt != 5", person.javaInt $ne 5);
  }
  
  @Test
  def Number_Comparison2 {
    assertEquals("person.scalaInt < 5", person.scalaInt < 5);
    assertEquals("person.javaInt < 5", person.javaInt < 5);
    assertEquals("person.javaInt > 5", person.javaInt > 5);
    assertEquals("person.javaInt <= 5", person.javaInt <= 5);
    assertEquals("person.javaInt >= 5", person.javaInt >= 5);
    assertEquals("person.javaInt = 5", person.javaInt $eq 5);
    assertEquals("person.javaInt != 5", person.javaInt $ne 5);
  }  
  
  @Test
  def Number_Between {
    assertEquals("person.scalaInt between 2 and 3", person.scalaInt between (2, 3));
    assertEquals("person.javaInt between 2 and 3", person.javaInt between (2, 3));
  }

  @Test
  def Number_Arithmetic {
    assertEquals("person.scalaInt + 3", person.scalaInt add 3);
    assertEquals("person.scalaInt - 3", person.scalaInt subtract 3);
    assertEquals("person.scalaInt / 3", person.scalaInt divide 3);
    assertEquals("person.scalaInt * 3", person.scalaInt multiply 3);
    assertEquals("person.scalaInt * -1", person.scalaInt negate);
    assertEquals("person.scalaInt % 4", person.scalaInt mod 4);
    assertEquals("round(person.scalaInt)", person.scalaInt round);
    assertEquals("floor(person.scalaInt)", person.scalaInt floor);
    assertEquals("ceil(person.scalaInt)", person.scalaInt ceil);
    assertEquals("sqrt(person.scalaInt)", person.scalaInt sqrt);
  }

  @Test
  def Number_Casts {
    assertEquals("cast(person.javaInt,class java.lang.Long)", person.javaInt $longValue);
    assertEquals("cast(person.scalaInt,class java.lang.Long)", person.scalaInt $longValue);
  }

  @Test
  def Java_Collections_Size {
    assertEquals("size(person.javaCollection)", person.javaCollection size);
    assertEquals("size(person.javaSet)", person.javaSet size);
    assertEquals("size(person.javaList)", person.javaList size);
    assertEquals("size(person.javaMap)", person.javaMap size);
  }

  @Test
  def Java_Collections_Is_Empty {
    assertEquals("empty(person.javaCollection)", person.javaCollection isEmpty);
    assertEquals("empty(person.javaSet)", person.javaSet isEmpty);
    assertEquals("empty(person.javaList)", person.javaList isEmpty);
    assertEquals("empty(person.javaMap)", person.javaMap isEmpty);
  }

  @Test
  def Java_Collections_Get {
    assertEquals("person.javaList.get(0) is not null", person.javaList.get(0) isNotNull);
    assertEquals("person.javaMap.get(xxx) is null", person.javaMap.get("xxx") isNull);
  }

  @Test
  def Java_Collections_Get_And_Starts_With {
    assertEquals("startsWith(person.javaMap.get(xxx),X)", person.javaMap.get("xxx") startsWith "X");
  }

  @Test
  def Scala_Collections_Size {
    assertEquals("size(person.scalaList)", person.scalaList size);
    assertEquals("size(person.scalaMap)", person.scalaMap size);
  }

  @Test
  def Scala_Collections_Is_Empty {
    assertEquals("empty(person.scalaList)", person.scalaList isEmpty);
    assertEquals("empty(person.scalaMap)", person.scalaMap isEmpty);
  }

  @Test
  def Scala_Collections_Get {
    assertEquals("person.scalaList.get(0) is not null", person.scalaList(0) isNotNull);
    assertEquals("person.scalaList.get(0) is not null", person.scalaList(0) isNotNull);
    assertEquals("person.scalaMap.get(xxx) is null", person.scalaMap("xxx") isNull);
  }

  @Test
  def Scala_Collections_Contains {
    assertEquals("X in person.scalaList", person.scalaList contains "X");
    assertEquals("X in person.javaList", person.javaList contains "X");
  }

  @Test
  def Scala_Collections_Get_And_Starts_With {
    assertEquals("startsWith(person.scalaMap.get(xxx),X)", person.scalaMap("xxx") startsWith "X");
  }

  @Test
  def Array_Size {
    val num: NumberExpression[Integer] = person.array size;   
    assertEquals("size(person.array)", num);
  }

  @Test
  def Complex_Starts_With_And_Less_Than_And_Is_Null() {
    val expr: Predicate = (person.firstName startsWith person.lastName) and (person.javaInt lt person.scalaInt) or (person.javaDouble isNull);
    assertEquals("startsWith(person.firstName,person.lastName) " +
         "&& person.javaInt < person.scalaInt " +
         "|| person.javaDouble is null", expr);
  }
  
  @Test
  def Lt_and_Gt() {
    val expr: Predicate = (person.javaInt < 5) and (person.scalaInt > 7);
    assertEquals("person.javaInt < 5 and person.scalaInt > 7", expr);
  }
  
  @Test
  def Gt_and_Lt() {
    val expr: Predicate = (person.javaInt > 5) and (person.scalaInt < 7);
    assertEquals("person.javaInt > 5 and person.scalaInt < 7", expr);
  }
  
  @Test
  def Lt_or_Gt() {
    val expr: Predicate = (person.javaInt < 5) or (person.scalaInt > 7);
    assertEquals("person.javaInt < 5 or person.scalaInt > 7", expr);
  }
  
  @Test
  def Gt_or_Lt() {
    val expr: Predicate = (person.javaInt > 5) or (person.scalaInt < 7);
    assertEquals("person.javaInt > 5 or person.scalaInt < 7", expr);
  }
  
  @Test
  def Starts_with() {
    assertEquals("startsWith(person.firstName,amin)",  person.firstName startsWith "amin");
  }
  
  @Test
  def Ends_with() {
    assertEquals("endsWith(person.firstName,amin)",  person.firstName endsWith "amin");
  }
  
  @Test
  def Prefix(){
    assertEquals("count(person)", count(person));
    assertEquals("min(person.javaInt)", min(person.javaInt));
    assertEquals("min(person.scalaInt)", min(person.scalaInt));
  }
  
}

