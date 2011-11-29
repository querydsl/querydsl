/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;
import TypeDefs._

/**
 * Factory for Operations
 * 
 * @author tiwe
 *
 */
object Operations {

  type Op[X] = Operator[X]
  
  def simple[T](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): SimpleExpression[T] = new OperationImpl[T](t, op, args: _*) with SimpleExpression[T]

  def comparable[T <: Comparable[_]](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): ComparableExpression[T] = new OperationImpl[T](t, op, args: _*) with ComparableExpression[T]

  def date[T <: Comparable[_]](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): DateExpression[T] = new OperationImpl[T](t, op, args: _*) with DateExpression[T]

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): DateTimeExpression[T] = new OperationImpl[T](t, op, args: _*) with DateTimeExpression[T]

  def time[T <: Comparable[_]](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): TimeExpression[T] = new OperationImpl[T](t, op, args: _*) with TimeExpression[T]

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], op: Op[_ >: T], args: Ex[_]*): NumberExpression[T] = 
      new OperationImpl[T](t, op, args: _*) with NumberExpression[T] {
        override def negate = if (getOperator == Ops.NEGATE) getArg(0).asInstanceOf[NumberExpression[T]] else super.negate;      
      }

  def boolean(op: Op[_ >: java.lang.Boolean], args: Ex[_]*): BooleanExpression = new OperationImpl[java.lang.Boolean](classOf[java.lang.Boolean], op, args: _*) with BooleanExpression

  def string(op: Op[_ >: String], args: Ex[_]*): StringExpression = new OperationImpl[String](classOf[String], op, args: _*) with StringExpression

  def enum[T <: Enum[T]](t: Class[T], op: Op[_ >: T], args: Ex[_]*): EnumExpression[T] = new OperationImpl[T](t, op, args: _*) with EnumExpression[T]

}

