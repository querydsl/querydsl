package com.mysema.query.scala;

import com.mysema.query._;
import com.mysema.query.types._;

object Queries {
    
  implicit def toScalaProjectable(qry: Projectable) = new ScalaProjectable(qry)
  
  implicit def toScalaProjectable[T](qry: SimpleProjectable[T]) = new SimpleScalaProjectable[T](qry);
    
}

class ScalaProjectable(val qry: Projectable) {
    
  def unique[T](e: Expression[T]) = Option(qry.uniqueResult(e))
    
  def single[T](e: Expression[T]) = Option(qry.singleResult(e))
    
}

class SimpleScalaProjectable[T](val qry: SimpleProjectable[T]) {
    
  def unique = Option(qry.uniqueResult())
    
  def single = Option(qry.singleResult())
    
}