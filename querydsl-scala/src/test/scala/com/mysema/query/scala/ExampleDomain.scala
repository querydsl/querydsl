package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.types.PathMetadataFactory._;

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
  
  var other: Person = _;
  
}

object Person {
    
  def as(path: String) = new QPerson(path);
  
}

object QPerson {
    def as(variable: String) = new QPerson(variable)
}

class QPerson(cl: Class[_ <: Person], md: PathMetadata[_]) extends EntityPathImpl[Person](cl, md) {

    def this(variable: String) = this(classOf[Person], forVariable(variable));
    
    def this(parent: Path[_], variable: String) = this(classOf[Person], forProperty(parent, variable));
    
    lazy val other = new QPerson(this, "other");

    lazy val firstName = createString("firstName");

    lazy val scalaMap = createMap("scalaMap", classOf[String], classOf[String], classOf[StringPath]);

    lazy val scalaInt = createNumber("scalaInt", classOf[Integer]);

    lazy val javaCollection = createCollection("javaCollection", classOf[String], classOf[StringPath]);

    lazy val javaInt = createNumber("javaInt", classOf[Integer]);

    lazy val scalaList = createList("scalaList", classOf[String], classOf[StringPath]);

    lazy val javaMap = createMap("javaMap", classOf[String], classOf[String], classOf[StringPath]);

    lazy val javaList = createList("javaList", classOf[String], classOf[StringPath]);

    lazy val javaDouble = createNumber("javaDouble", classOf[java.lang.Double]);

    lazy val javaSet = createSet("javaSet", classOf[String], classOf[StringPath]);

    lazy val lastName = createString("lastName");

    lazy val array = createArray("array", classOf[Array[String]]);

    lazy val listOfPersons = createList("listOfPersons", classOf[Person], classOf[QPerson]);

}

