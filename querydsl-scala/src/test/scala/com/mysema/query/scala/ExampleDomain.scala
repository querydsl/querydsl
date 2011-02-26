package com.mysema.query.scala;

import com.mysema.query.types._;

class Person {
  var scalaInt: Int = _;

  var javaInt: Integer = _;

  var javaDouble: java.lang.Double = _;
  
  var firstName: String = _;

  var lastName: String = _;

  var scalaList: scala.List[String] = _;

  var scalaMap: scala.collection.Map[String, String] = _;

  var javaCollection: java.util.Collection[String] = _;

  var javaSet: java.util.Set[String] = _;

  var javaList: java.util.List[String] = _;

  var javaMap: java.util.Map[String, String] = _;

  var listOfPersons: java.util.List[Person] = _;
  
  var array: Array[String] = _;
}

//class QPerson(md: PathMetadata[_]) extends EntityPathImpl[Person](classOf[Person], md) {
//    
//    def this(variable: String) = this(PathMetadataFactory.forVariable(variable));
//    
//}