/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;

/**
 * Factory for Operations
 * 
 * @author tiwe
 *
 */
object Operations {

  def simple[T](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): SimpleExpression[T] = new OperationImpl[T](t, op, args: _*) with SimpleExpression[T]

  def comparable[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): ComparableExpression[T] = new OperationImpl[T](t, op, args: _*) with ComparableExpression[T]

  def date[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): DateExpression[T] = new OperationImpl[T](t, op, args: _*) with DateExpression[T]

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): DateTimeExpression[T] = new OperationImpl[T](t, op, args: _*) with DateTimeExpression[T]

  def time[T <: Comparable[_]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): TimeExpression[T] = new OperationImpl[T](t, op, args: _*) with TimeExpression[T]

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], op: Operator[_ >: T], args: Expression[_]*): NumberExpression[T] = 
      new OperationImpl[T](t, op, args: _*) with NumberExpression[T] {
        override def negate = if (getOperator == Ops.NEGATE) getArg(0).asInstanceOf[NumberExpression[T]] else super.negate;      
      }

  def boolean(op: Operator[_ >: java.lang.Boolean], args: Expression[_]*): BooleanExpression = new OperationImpl[java.lang.Boolean](classOf[java.lang.Boolean], op, args: _*) with BooleanExpression

  def string(op: Operator[_ >: String], args: Expression[_]*): StringExpression = new OperationImpl[String](classOf[String], op, args: _*) with StringExpression

  def enum[T <: Enum[T]](t: Class[T], op: Operator[_ >: T], args: Expression[_]*): EnumExpression[T] = new OperationImpl[T](t, op, args: _*) with EnumExpression[T]

}

