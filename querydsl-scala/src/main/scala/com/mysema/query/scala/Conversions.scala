/*
 * Copyright (c) 2010 Mysema Ltd. 
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.alias._;

import com.mysema.query.types._

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
object Conversions {

  val aliasFactory = new AliasFactory(new PathFactoryImpl(), new TypeSystemImpl());

  def alias[T](cl: Class[T]): T = alias(cl, StringUtils.uncapitalize(cl.getSimpleName));

  def alias[T](cl: Class[T], variable: String): T = aliasFactory.createAliasForVariable(cl, variable);

  def alias[T](cl: Class[T], expr: Expression[_ <: T]): T = aliasFactory.createAliasForExpr(cl, expr);

  // prefix functions
  
  def not(b: BooleanExpression) = b.not;
  
  def count(e: SimpleExpression[_]) = e.count();
  
  def min(e: NumberExpression[_]) = e.min();

  def max(e: NumberExpression[_]) = e.max();
  
  def sum(e: NumberExpression[_]) = e.sum();
  
  def avg(e: NumberExpression[_]) = e.avg();  
  
  // implicit conversions

  implicit def arrayPath[T <: Array[_]](a: T): ArrayPath[T] = aliasFactory.getCurrentAndReset();

  implicit def booleanPath(b: java.lang.Boolean): BooleanFunctions = aliasFactory.getCurrentAndReset();

  implicit def stringPath(s: String): StringPath = aliasFactory.getCurrentAndReset();

  implicit def datePath(d: java.sql.Date): DatePath[java.sql.Date] = aliasFactory.getCurrentAndReset();

  implicit def dateTimePath(d: java.util.Date): DateTimePath[java.util.Date] = aliasFactory.getCurrentAndReset();

  implicit def timePath(t: java.sql.Time): TimePath[java.sql.Time] = aliasFactory.getCurrentAndReset();

  implicit def comparablePath(c: Comparable[_]): ComparablePath[_] = aliasFactory.getCurrentAndReset();

  implicit def numberPath[N <: Number with Comparable[N]](n: N): NumberPath[N] = aliasFactory.getCurrentAndReset();

  implicit def bytePath(n: Byte): NumberPath[java.lang.Byte] = aliasFactory.getCurrentAndReset();

  implicit def intPath(n: Int): NumberPath[Integer] = aliasFactory.getCurrentAndReset();

  implicit def longPath(n: Long): NumberPath[java.lang.Long] = aliasFactory.getCurrentAndReset();

  implicit def shortPath(n: Short): NumberPath[java.lang.Short] = aliasFactory.getCurrentAndReset();

  implicit def doublePath(n: Double): NumberPath[java.lang.Double] = aliasFactory.getCurrentAndReset();

  implicit def floatPath(n: Float): NumberPath[java.lang.Float] = aliasFactory.getCurrentAndReset();

  implicit def scalaCollectionPath[T](c: scala.Collection[T]): CollectionPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaListPath[T](l: scala.List[T]): ListPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaSetPath[T](c: scala.collection.Set[T]): SetPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaMapPath[K, V](l: scala.collection.Map[K, V]): MapPath[K, V] = aliasFactory.getCurrentAndReset();

  implicit def javaCollectionPath[T](c: java.util.Collection[T]): CollectionPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaListPath[T](l: java.util.List[T]): ListPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaSetPath[T](c: java.util.Set[T]): SetPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaMapPath[K, V](l: java.util.Map[K, V]): MapPath[K, V] = aliasFactory.getCurrentAndReset();

  //implicit def simplePath(s: Object): SimplePath[_] = aliasFactory.getCurrentAndReset();

  implicit def entityPath[T](arg: T): EntityPathImpl[T] = {
    val rv = Option(aliasFactory.getCurrentAndReset());
    rv.getOrElse(arg) match {
      case x: EntityPathImpl[T] => x;
      case x: ManagedObject => x.__mappedPath.asInstanceOf[EntityPathImpl[T]];
      case _ => null;
    }
  }

}

class PathFactoryImpl extends PathFactory {

  def createArrayPath[T](t: Class[Array[T with Object]], md: PathMetadata[_]) = Paths.array(t, md);

  def createEntityPath[T](t: Class[T], md: PathMetadata[_]) = Paths.entity(t, md);

  def createSimplePath[T](t: Class[T], md: PathMetadata[_]) = Paths.simple(t, md);

  def createComparablePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.comparable(t, md);

  def createEnumPath[T <: Enum[T]](t: Class[T], md: PathMetadata[_]) = Paths.enum(t, md);

  def createDatePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.date(t, md);

  def createTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.time(t, md);

  def createDateTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.dateTime(t, md);

  def createNumberPath[T <: Number with Comparable[T]](t: Class[T], md: PathMetadata[_]) = Paths.number(t, md);

  def createBooleanPath(md: PathMetadata[_]) = Paths.boolean(md);

  def createStringPath(md: PathMetadata[_]) = Paths.string(md);

  def createListPath[T](t: Class[T], md: PathMetadata[_]) = Paths.list(t, md);

  def createSetPath[T](t: Class[T], md: PathMetadata[_]) = Paths.set(t, md);

  def createCollectionPath[T](t: Class[T], md: PathMetadata[_]) = Paths.collection(t, md);

  def createMapPath[K, V](k: Class[K], v: Class[V], md: PathMetadata[_]) = Paths.map(k, v, md);

}

class TypeSystemImpl extends TypeSystem {

  val system = new DefaultTypeSystem();

  def isCollectionType(cl: Class[_]) = system.isCollectionType(cl) || classOf[scala.Collection[_]].isAssignableFrom(cl);

  def isSetType(cl: Class[_]) = system.isSetType(cl) || classOf[scala.collection.Set[_]].isAssignableFrom(cl);

  def isListType(cl: Class[_]) = system.isListType(cl) || classOf[scala.List[_]].isAssignableFrom(cl);

  def isMapType(cl: Class[_]) = system.isMapType(cl) || classOf[scala.collection.Map[_, _]].isAssignableFrom(cl);

}
