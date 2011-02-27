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
    private var _other: QPerson = _;

    def this(variable: String) = this(classOf[Person], forVariable(variable));
    
    def this(parent: Path[_], variable: String) = this(classOf[Person], forProperty(parent, variable));
    
    def other: QPerson = { if (_other == null){_other = new QPerson(this, "other"); }; _other; }

    val firstName = createString("firstName");

    val scalaMap = createMap("scalaMap", classOf[String], classOf[String], classOf[StringPath]);

    val scalaInt = createNumber("scalaInt", classOf[Integer]);

    val javaCollection = createCollection("javaCollection", classOf[String], classOf[StringPath]);

    val javaInt = createNumber("javaInt", classOf[Integer]);

    val scalaList = createList("scalaList", classOf[String], classOf[StringPath]);

    val javaMap = createMap("javaMap", classOf[String], classOf[String], classOf[StringPath]);

    val javaList = createList("javaList", classOf[String], classOf[StringPath]);

    val javaDouble = createNumber("javaDouble", classOf[java.lang.Double]);

    val javaSet = createSet("javaSet", classOf[String], classOf[StringPath]);

    val lastName = createString("lastName");

    val array = createArray("array", classOf[Array[String]]);

    val listOfPersons = createList("listOfPersons", classOf[Person], classOf[QPerson]);

}

