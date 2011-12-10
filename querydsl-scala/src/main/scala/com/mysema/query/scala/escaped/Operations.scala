/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mysema.query.scala.escaped;

import com.mysema.query.types._;

/**
 * @author tiwe
 *
 */
object Operations {

  def simple[T](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): SimpleExpression[T] = new OperationImpl[T](t, op, args: _*) with SimpleExpression[T];

  def comparable[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): ComparableExpression[T] = new OperationImpl[T](t, op, args: _*) with ComparableExpression[T];

  def date[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): DateExpression[T] = new OperationImpl[T](t, op, args: _*) with DateExpression[T];

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): DateTimeExpression[T] = new OperationImpl[T](t, op, args: _*) with DateTimeExpression[T];

  def time[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): TimeExpression[T] = new OperationImpl[T](t, op, args: _*) with TimeExpression[T];

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): NumberExpression[T] = new OperationImpl[T](t, op, args: _*) with NumberExpression[T];

  def boolean(op: Operator[_ >: java.lang.Boolean], args: Expression[_]*): BooleanExpression = new OperationImpl[java.lang.Boolean](classOf[java.lang.Boolean], op, args: _*) with BooleanExpression;

  def string(op: Operator[_ >: String], args: Expression[_]*): StringExpression = new OperationImpl[String](classOf[String], op, args: _*) with StringExpression;

  def enum[T <: Enum[T]](t: Class[T], op: Operator[_ >: T], args: Expression[_]*): EnumExpression[T] = new OperationImpl[T](t, op, args: _*) with EnumExpression[T];

}

