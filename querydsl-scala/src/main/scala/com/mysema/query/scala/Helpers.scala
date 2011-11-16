package com.mysema.query.scala

import scala.collection.JavaConversions._
import com.mysema.query._
import com.mysema.query.types._

object Helpers {
  
  implicit def toRichSimpleProjectable[T](p: SimpleProjectable[T]) = new RichSimpleProjectable(p) 
  
  implicit def toRichProjectable(p: Projectable) = new RichProjectable(p)
   
}

class RichSimpleProjectable[T](private val p: SimpleProjectable[T]) {
  
  def select: List[T] = p.list.toList
  
  def single: Option[T] = Option(p.singleResult())
  
  def unique: Option[T] = Option(p.uniqueResult())
  
}

class RichProjectable(private val p: Projectable) {
  
  def select[T](expr: Expression[T]): List[T] = p.list(expr).toList
  
  def select[T,U](expr1: Expression[T], expr2: Expression[U]): List[(T,U)] = {
    p.list(expr1, expr2).toList map(r => (r(0).asInstanceOf[T], r(1).asInstanceOf[U]))
  }
  
  def select[T,U,V](expr1: Expression[T], expr2: Expression[U], expr3: Expression[V]): List[(T,U,V)] = {
    p.list(expr1, expr2, expr3).toList map(r => (r(0).asInstanceOf[T], r(1).asInstanceOf[U], r(2).asInstanceOf[V]))
  }
  
  def select[T,U,V,W](expr1: Expression[T], expr2: Expression[U], expr3: Expression[V], expr4: Expression[W]): List[(T,U,V,W)] = {
    p.list(expr1, expr2, expr3, expr4)
      .map(r => (r(0).asInstanceOf[T], r(1).asInstanceOf[U], r(2).asInstanceOf[V], r(3).asInstanceOf[W])).toList
  }  
  
  // TODO : generalize this
  def selectGrouped[K,T,V](expr1: Expression[K], expr2: Expression[T], expr3: Expression[V]): List[(T,List[V])] = {
    select(expr1, expr2, expr3).groupBy(_._1).map(_._2).map(r => (r(0)._2, r.map(_._3))).toList 
  }  
  
  def single[T](expr: Expression[T]): Option[T] = Option(p.singleResult(expr))
  
  def unique[T](expr: Expression[T]): Option[T] = Option(p.uniqueResult(expr))
  
}
