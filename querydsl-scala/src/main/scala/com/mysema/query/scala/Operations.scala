/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;

/**
 * @author tiwe
 *
 */
object Operations {

  def simple[T](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): SimpleExpression[T] = new SimpleOperation[T](t, operator, args: _*);

  def comparable[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): ComparableExpression[T] = new ComparableOperation[T](t, operator, args: _*);

  def date[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): DateExpression[T] = new DateOperation[T](t, operator, args: _*);

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): DateTimeExpression[T] = new DateTimeOperation[T](t, operator, args: _*);

  def time[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): TimeExpression[T] = new TimeOperation[T](t, operator, args: _*);

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*): NumberExpression[T] = new NumberOperation[T](t, operator, args: _*);

  def boolean(operator: Operator[_ >: java.lang.Boolean], args: Expression[_]*): BooleanExpression = new BooleanOperation(operator, args: _*);

  def string(operator: Operator[_ >: String], args: Expression[_]*): StringExpression = new StringOperation(operator, args: _*);

  def enum[T <: Enum[T]](t: Class[T], operator: Operator[_ >: T], args: Expression[_]*): EnumExpression[T] = new EnumOperation[T](t, operator, args: _*);
}

class SimpleOperation[T](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with SimpleExpression[T] {

}

class ComparableOperation[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with ComparableExpression[T] {

}

class NumberOperation[T <: Number with Comparable[T]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with NumberExpression[T] {

}

class BooleanOperation(operator: Operator[_ >: java.lang.Boolean], args: Expression[_]*)
  extends OperationImpl[java.lang.Boolean](classOf[java.lang.Boolean], operator, args: _*) with BooleanExpression {

}

class StringOperation(operator: Operator[_ >: String], args: Expression[_]*)
  extends OperationImpl[String](classOf[String], operator, args: _*) with StringExpression {

}

class DateOperation[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with DateExpression[T] {

}

class DateTimeOperation[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with DateTimeExpression[T] {

}

class TimeOperation[T <: Comparable[_]](t: Class[_ <: T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with TimeExpression[T] {

}

class EnumOperation[T <: Enum[T]](t: Class[T], operator: Operator[_ >: T], args: Expression[_]*)
  extends OperationImpl[T](t, operator, args: _*) with EnumExpression[T] {

}
