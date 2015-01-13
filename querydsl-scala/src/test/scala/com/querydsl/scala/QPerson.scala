package com.querydsl.scala;

import com.querydsl.core.types._;
import com.querydsl.scala._;

import com.querydsl.core.types.PathMetadataFactory._;
import java.util.List;
import java.util.Map;

object QPerson extends QPerson("person"){
  override def as(variable: String) = new QPerson(variable)
  
}

class QPerson(cl: Class[_ <: Person], md: PathMetadata[_]) extends EntityPathImpl[Person](cl, md) {
  def this(variable: String) = this(classOf[Person], forVariable(variable))

  def this(parent: Path[_], variable: String) = this(classOf[Person], forProperty(parent, variable))

  lazy val other = new QPerson(this, "other")

  val array = createArray[Array[String]]("array")

  val firstName = createString("firstName")

  val javaCollection = createCollection[String,StringPath]("javaCollection")

  val javaDouble = createNumber[java.lang.Double]("javaDouble")

  val javaInt = createNumber[Integer]("javaInt")
  
  val javaList = createList[String,StringPath]("javaList")

  val javaMap = createMap[String,String,StringPath]("javaMap")

  val javaSet = createSet[String,StringPath]("javaSet")

  val lastName = createString("lastName")

  val listOfPersons = createList[Person,QPerson]("listOfPersons")

  val scalaInt = createNumber[Int]("scalaInt")
  
  val scalaDouble = createNumber[Double]("scalaDouble")
  
  //val num = createNumber[String]("x")

  val scalaList = createList[String,StringPath]("scalaList")

  val scalaMap = createMap[String,String,StringPath]("scalaMap")

}