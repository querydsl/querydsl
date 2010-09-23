package com.mysema.query.scala;

import com.mysema.query.scala.Conversions._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.types.Expression
import com.mysema.query.types.path._
import org.junit.Test

class AliasTest {

  var domainType = alias(classOf[DomainType])

  def assertEquals(expected: String, actual: Any) {
    org.junit.Assert.assertEquals(expected, actual.toString);
  }

  @Test
  def Path_Equality {
    assertEquals("domainType.firstName = domainType.lastName", domainType.firstName $eq domainType.lastName);
  }

  @Test
  def String_Equality {
    assertEquals("domainType.firstName = Hello", domainType.firstName $eq "Hello");
    assertEquals("domainType.firstName != Hello", domainType.firstName $ne "Hello");
    assertEquals("domainType.firstName != Hello", domainType.firstName $ne "Hello");
  }

  @Test
  def String_Like {
    assertEquals("domainType.firstName like Hello", domainType.firstName $like "Hello");
  }

  @Test
  def String_Order {
    assertEquals("domainType.firstName ASC", domainType.firstName asc);
  }

  @Test
  def String_Append {
    assertEquals("domainType.firstName + x", domainType.firstName $append "x");
    assertEquals("domainType.firstName +   + domainType.lastName", domainType.firstName $append " " $append domainType.lastName);
  }

  @Test
  def String_And {
    var andClause = (domainType.firstName $like "An%") $and (domainType.firstName $like "Be%");
    assertEquals("domainType.firstName like An% && domainType.firstName like Be%", andClause);
  }

  @Test
  def String_Or {
    var orClause = (domainType.firstName $like "An%") $or (domainType.firstName $like "Be%");
    assertEquals("domainType.firstName like An% || domainType.firstName like Be%", orClause);
  }

  @Test
  def String_Not {
    var notClause = (domainType.firstName $like "An%") not;
    assertEquals("!domainType.firstName like An%", notClause.toString);

    notClause = not(domainType.firstName $like "An%");
    assertEquals("!domainType.firstName like An%", notClause.toString);
  }

  @Test
  def String_Trim {
    assertEquals("trim(domainType.firstName)", domainType.firstName $trim);
  }

  @Test
  def String_Is_Empty {
    assertEquals("empty(domainType.firstName)", domainType.firstName $isEmpty);
  }

  @Test
  def Number_Comparison {
    assertEquals("domainType.scalaInt < 5", domainType.scalaInt $lt 5);
    assertEquals("domainType.javaInt < 5", domainType.javaInt $lt 5);
    assertEquals("domainType.javaInt > 5", domainType.javaInt $gt 5);
    assertEquals("domainType.javaInt <= 5", domainType.javaInt $loe 5);
    assertEquals("domainType.javaInt >= 5", domainType.javaInt $goe 5);
    assertEquals("domainType.javaInt = 5", domainType.javaInt $eq 5);
    assertEquals("domainType.javaInt != 5", domainType.javaInt $ne 5);
  }

  @Test
  def Number_Between {
    assertEquals("domainType.scalaInt between 2 and 3", domainType.scalaInt $between (2, 3));
    assertEquals("domainType.javaInt between 2 and 3", domainType.javaInt $between (2, 3));
  }

  @Test
  def Number_Arithmetic {
    assertEquals("domainType.scalaInt + 3", domainType.scalaInt $add 3);
    assertEquals("domainType.scalaInt - 3", domainType.scalaInt $subtract 3);
    assertEquals("domainType.scalaInt / 3", domainType.scalaInt $divide 3);
    assertEquals("domainType.scalaInt * 3", domainType.scalaInt $multiply 3);
    assertEquals("domainType.scalaInt * -1", domainType.scalaInt $negate);
    assertEquals("domainType.scalaInt % 4", domainType.scalaInt $mod 4);
    assertEquals("round(domainType.scalaInt)", domainType.scalaInt $round);
    assertEquals("floor(domainType.scalaInt)", domainType.scalaInt $floor);
    assertEquals("ceil(domainType.scalaInt)", domainType.scalaInt $ceil);
    assertEquals("sqrt(domainType.scalaInt)", domainType.scalaInt $sqrt);
  }

  @Test
  def Number_Casts {
    assertEquals("cast(domainType.javaInt,class java.lang.Long)", domainType.javaInt $longValue);
    assertEquals("cast(domainType.scalaInt,class java.lang.Long)", domainType.scalaInt $longValue);
  }

  @Test
  def Java_Collections_Size {
    assertEquals("size(domainType.javaCollection)", domainType.javaCollection $size);
    assertEquals("size(domainType.javaSet)", domainType.javaSet $size);
    assertEquals("size(domainType.javaList)", domainType.javaList $size);
    assertEquals("size(domainType.javaMap)", domainType.javaMap $size);
  }

  @Test
  def Java_Collections_Is_Empty {
    assertEquals("empty(domainType.javaCollection)", domainType.javaCollection $isEmpty);
    assertEquals("empty(domainType.javaSet)", domainType.javaSet $isEmpty);
    assertEquals("empty(domainType.javaList)", domainType.javaList $isEmpty);
    assertEquals("empty(domainType.javaMap)", domainType.javaMap $isEmpty);
  }

  @Test
  def Java_Collections_Get {
    assertEquals("domainType.javaList.get(0) is not null", domainType.javaList.get(0) $isNotNull);
    assertEquals("domainType.javaMap.get(xxx) is null", domainType.javaMap.get("xxx") $isNull);
  }

  @Test
  def Java_Collections_Get_And_Starts_With {
    assertEquals("startsWith(domainType.javaMap.get(xxx),X)", domainType.javaMap.get("xxx") $startsWith "X");
  }

  @Test
  def Scala_Collections_Size {
    assertEquals("size(domainType.scalaList)", domainType.scalaList $size);
    assertEquals("size(domainType.scalaMap)", domainType.scalaMap $size);
  }

  @Test
  def Scala_Collections_Is_Empty {
    assertEquals("empty(domainType.scalaList)", domainType.scalaList $isEmpty);
    assertEquals("empty(domainType.scalaMap)", domainType.scalaMap $isEmpty);
  }

  @Test
  def Scala_Collections_Get {
    assertEquals("domainType.scalaList.get(0) is not null", domainType.scalaList(0) $isNotNull);
    assertEquals("domainType.scalaList.get(0) is not null", domainType.scalaList(0) $isNotNull);
    assertEquals("domainType.scalaMap.get(xxx) is null", domainType.scalaMap("xxx") $isNull);
  }

  @Test
  def Scala_Collections_Contains {
    assertEquals("X in domainType.scalaList", domainType.scalaList $contains "X");
    assertEquals("X in domainType.javaList", domainType.javaList $contains "X");
  }

  @Test
  def Scala_Collections_Get_And_Starts_With {
    assertEquals("startsWith(domainType.scalaMap.get(xxx),X)", domainType.scalaMap("xxx") $startsWith "X");
  }

  @Test
  def Array_Size {
    assertEquals("size(domainType.array)", domainType.array $size ());
  }

  @Test
  def Complex_Expressions() {
      val expr = ((domainType.firstName $startsWith domainType.lastName) 
          $and (domainType.javaInt $lt domainType.scalaInt) 
          $or (domainType.javaDouble $isNull));
      assertEquals("startsWith(domainType.firstName,domainType.lastName) " +
         "&& domainType.javaInt < domainType.scalaInt " +
         "|| domainType.javaDouble is null", expr);
  }
}

