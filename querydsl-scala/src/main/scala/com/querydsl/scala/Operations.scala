/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

package com.querydsl.scala

import com.querydsl.core.types._

/**
 * Factory for Operations
 *
 * @author tiwe
 *
 */
object Operations {

  type Op = Operator

  def dsl[T](t: Class[_ <: T], op: Op, args: Ex[_]*): DslExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with DslExpression[T]
  }

  def simple[T](t: Class[_ <: T], op: Op, args: Ex[_]*): SimpleExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with SimpleExpression[T]
  }

  def comparable[T <: Comparable[_]](t: Class[_ <: T], op: Op, args: Ex[_]*): ComparableExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with ComparableExpression[T]
  }

  def date[T <: Comparable[_]](t: Class[_ <: T], op: Op, args: Ex[_]*): DateExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with DateExpression[T]
  }

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], op: Op, args: Ex[_]*): DateTimeExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with DateTimeExpression[T]
  }

  def time[T <: Comparable[_]](t: Class[_ <: T], op: Op, args: Ex[_]*): TimeExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with TimeExpression[T]
  }

  def number[T : Numeric](t: Class[_ <: T], op: Op, args: Ex[_]*): NumberExpression[T] =
      new OperationImpl[T](t, op, args: _*) with NumberExpression[T] {
        override def negate = if (getOperator == Ops.NEGATE) getArg(0).asInstanceOf[NumberExpression[T]] else super.negate
        override def numeric = implicitly[Numeric[T]]
      }

  def boolean(op: Op, args: Ex[_]*): BooleanExpression = {
    new OperationImpl[java.lang.Boolean](classOf[java.lang.Boolean], op, args: _*) with BooleanExpression
  }

  def string(op: Op, args: Ex[_]*): StringExpression = {
    new OperationImpl[String](classOf[String], op, args: _*) with StringExpression
  }

  def enum[T <: Enum[T]](t: Class[T], op: Op, args: Ex[_]*): EnumExpression[T] = {
    new OperationImpl[T](t, op, args: _*) with EnumExpression[T]
  }

}

