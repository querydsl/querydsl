package com.querydsl.scala;

import com.querydsl.core.types._;
import com.querydsl.core.types.PathMetadataFactory._;

class Person {
  var scalaInt: Int = _

  var javaInt: Integer = _

  var javaDouble: java.lang.Double = _
  
  var firstName: String = _

  var lastName: String = _

  var scalaList: scala.List[String] = _

  var scalaMap: scala.collection.Map[String, String] = _

  var javaCollection: java.util.Collection[String] = _

  var javaSet: java.util.Set[String] = _

  var javaList: java.util.List[String] = _

  var javaMap: java.util.Map[String, String] = _

  var listOfPersons: java.util.List[Person] = _
  
  var array: Array[String] = _
  
  var other: Person = _
  
}

object Person {
    
  def as(path: String) = new QPerson(path)
  
}

