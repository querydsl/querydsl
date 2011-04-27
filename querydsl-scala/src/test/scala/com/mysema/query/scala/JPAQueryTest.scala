package com.mysema.query.scala

import com.mysema.query.scala.Conversions._
import com.mysema.query.jpa.JPQLSubQuery;

import com.mysema.query.types.path._
import org.junit.Test
import org.junit.Assert._

class JPAQueryTest {

  var person = alias(classOf[Person])

  @Test
  def OneLiner {
    query from (person) where (person.firstName $like "Rob%") unique (person)
  }

  @Test
  def Joins {
      // FIXME
//    query from (person) innerJoin (person.listOfPersons, person)
//    query from (person) join (person.listOfPersons, person)
//    query from (person) leftJoin (person.listOfPersons, person)
//    query from (person) fullJoin (person.listOfPersons, person)
  }
  
  @Test
  def Projections {
    query from (person) list (person)
    query from (person) list (person.firstName ~)
    query from (person) list (person.firstName ~ , person.lastName ~) // because list(Object... args) is available for subqueries
  }

  @Test
  def Filters {
    query from (person) where (person.firstName $isEmpty()) count;
    query from (person) where (person.firstName $isEmpty(), person.lastName $isNotNull()) list (person);
    query from (person) where (person.firstName $startsWith "X") count;
    query from (person) where (person.javaInt $lt 5) count;
  }

  @Test
  def Order {
    query from (person) orderBy (person.firstName asc) list (person)
  }

  @Test
  def Various {
    // list
    query from (person) where (person.firstName $like "Rob%") list (person)
    // unique result
    query from (person) where (person.firstName $like "Rob%") unique (person)
    // long where
    query from (person) where (person.firstName $like "Rob%", person.lastName $like "An%") list (person)
  }
  
  @Test
  def Complex {
      query from person where ((person.firstName $like "An%") $and (person.lastName $isNotNull)) list person      
  }

  def query() = new JPQLSubQuery()

}