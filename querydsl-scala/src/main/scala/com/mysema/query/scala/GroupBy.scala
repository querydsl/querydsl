/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._
import com.mysema.query.group._

import scala.collection.immutable.{ List, Set, Map }
import scala.collection.mutable.{ ListBuffer, Set => MSet, Map => MMap }
import TypeDefs._

object GroupBy extends GroupBy

/**
 * Group by result transforming functionality
 * 
 * @author tiwe
 *
 */
trait GroupBy {
  
  def groupBy[K](key: Ex[K]) = new GroupByBuilder[K](key)
  
  def set[T](e: Ex[T]) = new AbstractGroupExpression[T,Set[T]](classOf[Set[T]], e) {
    def createGroupCollector() : GroupCollector[T,Set[T]] = new GroupCollector[T, Set[T]]() {
      val set = MSet[T]()
      def add(o: T): Unit = set.+=(o) 
      def get(): Set[T] = set.toSet
    }  
  }
    
  def list[T](e: Ex[T]) = new AbstractGroupExpression[T,List[T]](classOf[List[T]], e) {
    def createGroupCollector(): GroupCollector[T,List[T]] = new GroupCollector[T, List[T]]() {
      val list = new ListBuffer[T]()
      def add(o: T): Unit = list.+=(o) 
      def get(): List[T] = list.toList
    }  
  }
      
  def map[K,V](k: Ex[K], v: Ex[V]) = new AbstractGroupExpression[(K,V),Map[K,V]](classOf[Map[K,V]], new Tu2Ex(k,v)) {
    def createGroupCollector(): GroupCollector[(K,V),Map[K,V]] = new GroupCollector[(K,V),Map[K,V]]() {
      val map = MMap[K,V]()
      def add(tu: (K,V)) = map.+=(tu)
      def get(): Map[K,V] = map.toMap
    }        
  }
    
}

abstract class AbstractGroupExpression[T, R](cl: Class[R], expr: Ex[T]) extends GroupExpression[T, R] {
    
  def getExpression(): Ex[T] = expr
  
  def getType: Class[R] = cl
  
  def accept[R,C](v: Visitor[R,C], context: C): R = expr.accept(v, context)
  
  override def equals(o: Any): Boolean = {
    o match {
      case ge: GroupExpression[_,_] => ge.getExpression == expr
      case _ => false
    }
  }
  
  override def hashCode(): Int = expr.hashCode()
    
}