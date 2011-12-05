/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala

import com.mysema.query.types._

/**
 * Various factory methods for Expression DSL
 * 
 * @author tiwe
 *
 */
object Matchers {
    
  def in[T](values: T*) = (expr: SimpleExpression[T]) => expr.in(values:_*)  
  
  def not(v: Void) = (expr: SimpleExpression[_]) => expr.isNotNull
    
  def between[T <: Comparable[T]](left: T, right: T) = (expr: ComparableExpression[T]) => expr.between(left, right)
  
  def between(left: Number, right: Number) = (expr: NumberExpression[_]) => expr.between(left, right)
    
  def like(str: String) = (expr: StringExpression) => expr.like(str)
  
  def matches(str: String) = (expr: StringExpression) => expr.matches(str)
  
  def startsWith(str: String) = (expr: StringExpression) => expr.startsWith(str)
  
  def endsWith(str: String) = (expr: StringExpression) => expr.endsWith(str)
  
  // NOTE : clashes with CollectionExpression.contains
  def contains(str: String) = (expr: StringExpression) => expr.contains(str)
  
  // NOTE : clashes with MapExpression.isEmpty and CollectionExpression.isEmpty
  def empty = (expr: StringExpression) => expr.isEmpty
      
}