/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.querydsl.scala

import com.querydsl.core.types._

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
  
  def between[U : Numeric](left: U, right: U) = (expr: NumberExpression[_]) => expr.between(left, right)
    
  def like(str: String) = (expr: StringExpression) => expr.like(str)
  
  def matches(str: String) = (expr: StringExpression) => expr.matches(str)
  
  def startsWith(str: String) = (expr: StringExpression) => expr.startsWith(str)
  
  def endsWith(str: String) = (expr: StringExpression) => expr.endsWith(str)
  
  // NOTE : clashes with CollectionExpression.contains
  def contains(str: String) = (expr: StringExpression) => expr.contains(str)
  
  // NOTE : clashes with MapExpression.isEmpty and CollectionExpression.isEmpty
  def empty = (expr: StringExpression) => expr.isEmpty
      
}