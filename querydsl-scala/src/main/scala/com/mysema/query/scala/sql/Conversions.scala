package com.mysema.query.scala.sql

import com.mysema.query.types.expr._
import com.mysema.query.sql._

object Conversions {
    
  def not(b: BooleanExpression) = b.not;
  
  def count(e: SimpleExpression[_]) = e.count();
  
  def min(e: NumberExpression[_]) = e.min();

  def max(e: NumberExpression[_]) = e.max();
  
  def sum(e: NumberExpression[_]) = e.sum();
  
  def avg(e: NumberExpression[_]) = e.avg();  

}