package com.mysema.query.scala

import com.mysema.query.types._

object Matchers {
    
//  def in[T](values: T*) = (expr: SimpleExpression[T]) => expr.in(values);  
    
  // map
  
  // list
  
  // comparable
    
  def between[T <: Comparable[T]](left: T, right: T) = (expr: ComparableExpression[T]) => expr.between(left, right);
  
  def between(left: Number, right: Number) = (expr: NumberExpression[_]) => expr.between(left, right);
    
  // string  
    
  def like(str: String) = (expr: StringExpression) => expr.like(str);
  
  def matches(str: String) = (expr: StringExpression) => expr.matches(str);
  
  def startsWith(str: String) = (expr: StringExpression) => expr.startsWith(str);
  
  def endsWith(str: String) = (expr: StringExpression) => expr.endsWith(str);
  
  def contains(str: String) = (expr: StringExpression) => expr.contains(str);
  
  // NOTE : clashes with MapExpression.isEmpty and CollectionExpression.isEmpty
  def empty = (expr: StringExpression) => expr.isEmpty;
      
}