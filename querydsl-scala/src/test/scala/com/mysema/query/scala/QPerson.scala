package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.scala._;

import com.mysema.query.types.PathMetadataFactory._;
import java.util.List;
import java.util.Map;

object QPerson {
  def as(variable: String) = new QPerson(variable)
}

class QPerson(cl: Class[_ <: Person], md: PathMetadata[_]) extends EntityPathImpl[Person](cl, md) {
  def this(variable: String) = this(classOf[Person], forVariable(variable))

  def this(parent: Path[_], variable: String) = this(classOf[Person], forProperty(parent, variable))

  lazy val other = new QPerson(this, "other")

  val array = createArray("array", classOf[Array[String]])

  val firstName = createString("firstName")

  val javaCollection = createCollection("javaCollection", classOf[String], classOf[StringPath])

  val javaDouble = createNumber("javaDouble", classOf[java.lang.Double])

  val javaInt = createNumber("javaInt", classOf[Integer])

  val javaList = createList("javaList", classOf[String], classOf[StringPath])

  val javaMap = createMap("javaMap", classOf[String], classOf[String], classOf[StringPath])

  val javaSet = createSet("javaSet", classOf[String], classOf[StringPath])

  val lastName = createString("lastName")

  val listOfPersons = createList("listOfPersons", classOf[Person], classOf[QPerson])

  val scalaInt = createNumber("scalaInt", classOf[Integer])

  val scalaList = createList("scalaList", classOf[String], classOf[StringPath])

  val scalaMap = createMap("scalaMap", classOf[String], classOf[String], classOf[StringPath])

}