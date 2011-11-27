package com.mysema.query.scala;

import com.mysema.query.types._
import com.mysema.query.group._

import scala.collection.immutable.{ List, Set, Map }
import scala.collection.mutable.{ ListBuffer, Set => MSet, Map => MMap }
import TypeDefs._

object GroupBy extends GroupBy

trait GroupBy {
  
  def groupBy[K](key: Ex[K]) = new GroupByBuilder[K](key)
  
  def set[T](e: Ex[T]) = new GSet[T](e)
  
  def list[T](e: Ex[T]) = new GList[T](e)
  
  def map[K,V](k: Ex[K], v: Ex[V]) = new GMap[K,V](new Tu2Ex(k,v))  
  
}

abstract class AbstractGroupExpression[T, R](t: Class[R], expr: Ex[T]) extends GroupExpression[T, R] {
    
  def getExpression(): Ex[T] = expr
  
  def getType: Class[R] = t
  
  def accept[R,C](v: Visitor[R,C], context: C): R = expr.accept(v, context) 
  
  override def equals(o: Any): Boolean = {
    if (getClass() == o.getClass) o.asInstanceOf[GroupExpression[_,_]].getExpression() == expr
    else false
  }
  
  override def hashCode(): Int = expr.hashCode()
    
}

class GList[T](e: Ex[T]) extends AbstractGroupExpression[T, List[T]](classOf[List[T]], e) {
    
  def createGroupCollector() = {    
    new GroupCollector[T, List[T]]() {
      val list = new ListBuffer[T]()
      def add(o: T): Unit = list.+=(o) 
      def get(): List[T] = list.toList
    }
  }  
}

class GSet[T](e: Ex[T]) extends AbstractGroupExpression[T, Set[T]](classOf[Set[T]], e) {
  
  def createGroupCollector() = {    
    new GroupCollector[T, Set[T]]() {
      val set = MSet[T]()
      def add(o: T): Unit = set.+=(o) 
      def get(): Set[T] = set.toSet
    }
  }  
}

class GMap[K,V](tu2ex: Tu2Ex[K,V,_,_]) extends AbstractGroupExpression[(K,V), Map[K,V]](classOf[Map[K,V]], tu2ex) {
  
  def createGroupCollector() = {
    new GroupCollector[(K,V), Map[K,V]]() {
      val map = MMap[K,V]()
      def add(tu: (K,V)) = map.+=(tu)
      def get(): Map[K,V] = map.toMap
    }
  }  
}

