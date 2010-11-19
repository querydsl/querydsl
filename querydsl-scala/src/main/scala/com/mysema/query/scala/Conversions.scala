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

  val aliasFactory = new AliasFactory(new escaped.PathFactoryImpl(), new TypeSystemImpl());

  def alias[T](cl: Class[T]): T = alias(cl, StringUtils.uncapitalize(cl.getSimpleName));

  def alias[T](cl: Class[T], variable: String): T = aliasFactory.createAliasForVariable(cl, variable);

  def alias[T](cl: Class[T], expr: Expression[_ <: T]): T = aliasFactory.createAliasForExpr(cl, expr);

  // prefix functions
  
  def not(b: escaped.BooleanExpression) = b.not;
  
  def count(e: escaped.SimpleExpression[_]) = e.$count();
  
  def min(e: escaped.NumberExpression[_]) = e.$min();

  def max(e: escaped.NumberExpression[_]) = e.$max();
  
  def sum(e: escaped.NumberExpression[_]) = e.$sum();
  
  def avg(e: escaped.NumberExpression[_]) = e.$avg();  
  
  // implicit conversions

  implicit def arrayPath[T <: Array[_]](a: T): escaped.ArrayPath[T] = aliasFactory.getCurrentAndReset();

  implicit def booleanPath(b: java.lang.Boolean): escaped.BooleanPath = aliasFactory.getCurrentAndReset();

  implicit def stringPath(s: String): escaped.StringPath = aliasFactory.getCurrentAndReset();

  implicit def datePath(d: java.sql.Date): escaped.DatePath[java.sql.Date] = aliasFactory.getCurrentAndReset();

  implicit def dateTimePath(d: java.util.Date): escaped.DateTimePath[java.util.Date] = aliasFactory.getCurrentAndReset();

  implicit def timePath(t: java.sql.Time): escaped.TimePath[java.sql.Time] = aliasFactory.getCurrentAndReset();

  implicit def comparablePath(c: Comparable[_]): escaped.ComparablePath[_] = aliasFactory.getCurrentAndReset();

  implicit def numberPath[N <: Number with Comparable[N]](n: N): escaped.NumberPath[N] = aliasFactory.getCurrentAndReset();

  implicit def bytePath(n: Byte): escaped.NumberPath[java.lang.Byte] = aliasFactory.getCurrentAndReset();

  implicit def intPath(n: Int): escaped.NumberPath[Integer] = aliasFactory.getCurrentAndReset();

  implicit def longPath(n: Long): escaped.NumberPath[java.lang.Long] = aliasFactory.getCurrentAndReset();

  implicit def shortPath(n: Short): escaped.NumberPath[java.lang.Short] = aliasFactory.getCurrentAndReset();

  implicit def doublePath(n: Double): escaped.NumberPath[java.lang.Double] = aliasFactory.getCurrentAndReset();

  implicit def floatPath(n: Float): escaped.NumberPath[java.lang.Float] = aliasFactory.getCurrentAndReset();

  implicit def scalaCollectionPath[T](c: scala.Collection[T]): escaped.CollectionPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaListPath[T](l: scala.List[T]): escaped.ListPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaSetPath[T](c: scala.collection.Set[T]): escaped.SetPath[T] = aliasFactory.getCurrentAndReset();

  implicit def scalaMapPath[K, V](l: scala.collection.Map[K, V]): escaped.MapPath[K, V] = aliasFactory.getCurrentAndReset();

  implicit def javaCollectionPath[T](c: java.util.Collection[T]): escaped.CollectionPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaListPath[T](l: java.util.List[T]): escaped.ListPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaSetPath[T](c: java.util.Set[T]): escaped.SetPath[T] = aliasFactory.getCurrentAndReset();

  implicit def javaMapPath[K, V](l: java.util.Map[K, V]): escaped.MapPath[K, V] = aliasFactory.getCurrentAndReset();

  //implicit def simplePath(s: Object): SimplePath[_] = aliasFactory.getCurrentAndReset();

  implicit def entityPath[T](arg: T): escaped.EntityPathImpl[T] = {
    val rv = Option(aliasFactory.getCurrentAndReset());
    rv.getOrElse(arg) match {
      case x: escaped.EntityPathImpl[T] => x;
      case x: ManagedObject => x.__mappedPath.asInstanceOf[escaped.EntityPathImpl[T]];
      case _ => null;
    }
  }

}

class TypeSystemImpl extends TypeSystem {

  val system = new DefaultTypeSystem();

  def isCollectionType(cl: Class[_]) = system.isCollectionType(cl) || classOf[scala.Collection[_]].isAssignableFrom(cl);

  def isSetType(cl: Class[_]) = system.isSetType(cl) || classOf[scala.collection.Set[_]].isAssignableFrom(cl);

  def isListType(cl: Class[_]) = system.isListType(cl) || classOf[scala.List[_]].isAssignableFrom(cl);

  def isMapType(cl: Class[_]) = system.isMapType(cl) || classOf[scala.collection.Map[_, _]].isAssignableFrom(cl);

}
