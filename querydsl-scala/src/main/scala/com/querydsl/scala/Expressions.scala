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

package com.querydsl.scala

import com.querydsl.core.alias.ManagedObject

import com.querydsl.core.types._
import com.querydsl.core.types.PathMetadataFactory._
import com.querydsl.core.types.Ops._

import java.util.Collection
import java.util.Arrays._;

import Constants._
import Operations._
//import Conversions.aliasFactory;

object Constants {

  def constant(value: Integer) = ConstantImpl.create(value.intValue)

  def constant(value: String) = ConstantImpl.create(value)

  def constant[T](value: T) = new ConstantImpl(value)

}

trait DslExpression[T] extends Expression[T] {

  def as(right: Path[T]): DslExpression[T] = dsl(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  def as(alias: String): DslExpression[T] = as(new PathImpl[T](getType, alias))

}

trait SimpleExpression[T] extends DslExpression[T] {

  override def as(right: Path[T]): SimpleExpression[T] = simple(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): SimpleExpression[T] = as(new PathImpl[T](getType, alias))

  def eq(right: T): BooleanExpression = eq(constant(right))

  def eq(right: Expression[T]) = boolean(EQ, this, right)

  def ===(right: T) = eq(right)

  def ===(right: Expression[T]) = eq(right)

  def ne(right: T): BooleanExpression = ne(constant(right))

  def ne(right: Expression[T]) = boolean(NE, this, right)

  def !==(right: T) = ne(right)

  def !==(right: Expression[T]) = ne(right)

  lazy val count = number[Long](classOf[Long], AggOps.COUNT_AGG, this)

  def in(right: Collection[T]) = boolean(IN, this, constant(right))

  def in(right: T*): BooleanExpression = in(asList(right: _*))

  def in(right: CollectionExpression[T,_]) = boolean(IN, this, right)

  def countDistinct() = number[Long](classOf[Long], AggOps.COUNT_DISTINCT_AGG, this)

  lazy val isNotNull = boolean(IS_NOT_NULL, this)

  lazy val isNull = boolean(IS_NULL, this)

  def notIn(right: Collection[T]) = in(right).not

  def notIn(right: T*) = in(right: _*).not

  def notIn(right: CollectionExpression[T,_]) = in(right).not

  def nullif(right: T): SimpleExpression[T] = nullif(constant(right))

  def nullif(right: Expression[T]) = simple[T](getType, NULLIF.asInstanceOf[Operator[T]], this, right)

  def is[M <: SimpleExpression[T]](f: M => BooleanExpression): BooleanExpression = {
    if (f == null) isNull else f(this.asInstanceOf[M])
  }

  def not[M <: SimpleExpression[T]](f: M => BooleanExpression): BooleanExpression = f(this.asInstanceOf[M]).not

}

trait ArrayExpression[T <: Array[_]] extends DslExpression[T] {

  lazy val size = number[Integer](classOf[Integer], Ops.ARRAY_SIZE, this)

}

trait CollectionExpressionBase[T <: Collection[C], C, Q <: Expression[_ >: C]]
  extends DslExpression[T] with com.querydsl.core.types.CollectionExpression[T,C] {

  lazy val size = number[Int](classOf[Int], COL_SIZE, this)

  lazy val isEmpty = boolean(COL_IS_EMPTY, this)

  lazy val isNotEmpty = isEmpty.not

  def contains(child: C): BooleanExpression = contains(constant(child))

  def contains(child: Expression[C]) = boolean(IN, child, this)

  def any(): Q

}

trait CollectionExpression[T, Q <: Expression[_ >: T]] extends CollectionExpressionBase[java.util.Collection[T], T, Q] {}

trait SetExpression[T, Q <: Expression[_ >: T]] extends CollectionExpressionBase[java.util.Set[T], T, Q] {}

trait ListExpression[T, Q <: Expression[_ >: T]] extends CollectionExpressionBase[java.util.List[T], T, Q] {

  def get(i: Int): Q

  def get(i: Expression[Integer]): Q

  def apply(i: Int) = get(i)

  def apply(i: Expression[Integer]) = get(i)

}

trait MapExpression[K, V, Q <: Expression[_ >: V]]
  extends DslExpression[java.util.Map[K, V]] with com.querydsl.core.types.MapExpression[K,V] {

  lazy val size = number[Int](classOf[Int], MAP_SIZE, this)

  lazy val isEmpty = boolean(MAP_IS_EMPTY, this)

  lazy val isNotEmpty = isEmpty.not

  def containsKey(k: K) = boolean(CONTAINS_KEY, this, constant(k))

  def containsKey(k: Expression[K]) = boolean(CONTAINS_KEY, this, k)

  def containsValue(v: V) = boolean(CONTAINS_KEY, this, constant(v))

  def containsValue(v: Expression[V]) = boolean(CONTAINS_KEY, this, v)

  def get(key: K): Q

  def get(key: Expression[K]): Q

  def apply(key: K) = get(key)

  def apply(key: Expression[K]) = get(key)

}

trait ComparableExpressionBase[T <: Comparable[_]] extends SimpleExpression[T] {

  lazy val asc = new OrderSpecifier[T](Order.ASC, this)

  lazy val desc = new OrderSpecifier[T](Order.DESC, this)

}

trait ComparableExpression[T <: Comparable[_]] extends ComparableExpressionBase[T] {

  def lt(right: T): BooleanExpression = lt(constant(right))

  def lt(right: Expression[T]): BooleanExpression = boolean(LT, this, right)

  def <(right: T) = lt(right)

  def <(right: Expression[T]) = lt(right)

  def between(left: T, right: T): BooleanExpression = between(constant(left), constant(right))

  def between(left: Expression[T], right: Expression[T]) = boolean(BETWEEN, this, left, right)

  def notBetween(left: T, right: T): BooleanExpression = notBetween(constant(left), constant(right))

  def notBetween(left: Expression[T], right: Expression[T]) = between(left, right).not

  def gt(right: T): BooleanExpression = gt(constant(right))

  def gt(right: Expression[T]) = boolean(GT, this, right)

  def >(right: T) = gt(right)

  def >(right: Expression[T]) = gt(right)

  def goe(right: T): BooleanExpression = goe(constant(right))

  def goe(right: Expression[T]) = boolean(GOE, this, right)

  def >=(right: T) = goe(right)

  def >=(right: Expression[T]) = goe(right)

  def loe(right: T): BooleanExpression = loe(constant(right))

  def loe(right: Expression[T]) = boolean(LOE, this, right)

  def <=(right: T) = loe(right)

  def <=(right: Expression[T]) = loe(right)

  override def as(right: Path[T]) = comparable(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): ComparableExpression[T] = as(new PathImpl[T](getType, alias))

}

trait NumberExpression[T] extends SimpleExpression[T] {

  implicit def numeric: Numeric[T] // to be implemented in classes

  lazy val asc = new OrderSpecifier[java.lang.Double](Order.ASC, this.asInstanceOf[Ex[java.lang.Double]])

  lazy val desc = new OrderSpecifier[java.lang.Double](Order.DESC, this.asInstanceOf[Ex[java.lang.Double]])

  def add[U : Numeric](right: Ex[U]) = number[T](getType, Ops.ADD, this, right)

  def add[U : Numeric](right: U): NumberExpression[T] = add(constant(right))

  def +[U : Numeric](right: Ex[U]) = add(right)

  def +[U : Numeric](right: U) = add(right)

  def goe[U : Numeric](right: U): BooleanExpression = goe(constant(right))

  def goe[U : Numeric](right: Ex[U]) = boolean(Ops.GOE, this, right)

  def >=[U : Numeric](right: U) = goe(right)

  def >=[U : Numeric](right: Ex[U]) = goe(right)

  def gt[U : Numeric](right: U): BooleanExpression = gt(constant(right))

  def gt[U : Numeric](right: Ex[U]) = boolean(Ops.GT, this, right)

  def >[U : Numeric](right: U) = gt(right)

  def >[U : Numeric](right: Ex[U]) = gt(right)

  def between[U : Numeric](left: U, right: U) = boolean(Ops.BETWEEN, this, constant(left), constant(right))

  def between[U : Numeric](left: Ex[U], right: Ex[U]) = boolean(Ops.BETWEEN, this, left, right)

  def notBetween[U : Numeric](left: U, right: U): BooleanExpression = between(left, right).not

  def notBetween[U : Numeric](left: Ex[U], right: Ex[U]) = between(left, right).not

  def loe[U : Numeric](right: U): BooleanExpression = loe(constant(right))

  def loe[U : Numeric](right: Ex[U]) = boolean(Ops.LOE, this, right)

  def <=[U : Numeric](right: U) = loe(right)

  def <=[U : Numeric](right: Ex[U]) = loe(right)

  def lt[U : Numeric](right: U): BooleanExpression = lt(constant(right))

  def lt[U : Numeric](right: Ex[U]) = boolean(Ops.LT, this, right)

  def <[U : Numeric](right: U) = lt(right)

  def <[U : Numeric](right: Ex[U]) = lt(right)

  def in[U : Numeric](right: Array[U]) = boolean(IN, this, constant(asList(right: _*)))

  def like(right: String): BooleanExpression = like(constant(right))

  def like(right: Expression[String]) = boolean(Ops.LIKE, this, right)

  lazy val min = number[T](getType, AggOps.MIN_AGG, this)

  lazy val max = number[T](getType, AggOps.MAX_AGG, this)

  lazy val sum = number[T](getType, AggOps.SUM_AGG, this)

  lazy val avg = number[T](getType, AggOps.AVG_AGG, this)

  def subtract[U : Numeric](right: Ex[U]) = number[T](getType, Ops.SUB, this, right)

  def subtract[U : Numeric](right: U): NumberExpression[T] = subtract(constant(right))

  def -[U : Numeric](right: Ex[U]) = subtract(right)

  def -[U : Numeric](right: U) = subtract(right)

  def notIn[U : Numeric](right: Array[U]) = boolean(IN, this, constant(asList(right: _*))).not

  def divide[U : Numeric](right: Ex[U]) = number[T](getType, Ops.DIV, this, right)

  def divide[U : Numeric](right: U): NumberExpression[T] = divide(constant(right))

  def /[U : Numeric](right: Ex[U]) = divide(right)

  def /[U : Numeric](right: U) = divide(right)

  def multiply[U : Numeric](right: Ex[U]) = number[T](getType, Ops.MULT, this, right)

  def multiply[U : Numeric](right: U): NumberExpression[T] = multiply(constant(right))

  def *[U : Numeric](right: Ex[U]) = multiply(right)

  def *[U : Numeric](right: U) = multiply(right)

  def negate = number[T](getType, Ops.NEGATE, this)

  def mod[U : Numeric](right: Ex[U]) = number[T](getType, Ops.MOD, this, right)

  def mod[U : Numeric](right: U): NumberExpression[T] = mod(constant(right))

  def %[U : Numeric](right: Ex[U]) = mod(right)

  def %[U : Numeric](right: U) = mod(right)

  lazy val sqrt = number[Double](classOf[Double], MathOps.SQRT, this)

  lazy val abs = number[T](getType, MathOps.ABS, this)

  lazy val byteValue = castToNum(classOf[Byte])

  lazy val doubleValue = castToNum(classOf[Double])

  lazy val floatValue = castToNum(classOf[Float])

  lazy val intValue = castToNum(classOf[Int])

  lazy val longValue = castToNum(classOf[Long])

  lazy val shortValue = castToNum(classOf[Short])

  lazy val ceil = number[T](getType, MathOps.CEIL, this)

  lazy val floor = number[T](getType, MathOps.FLOOR, this)

  lazy val round = number[T](getType, MathOps.ROUND, this)

  def unary_-() = negate

  private def castToNum[A : Numeric](t: Class[A]): NumberExpression[A] = {
    if (t.equals(getType)) {
      this.asInstanceOf[NumberExpression[A]]
    } else {
      number[A](t, Ops.NUMCAST, this, constant(t))
    }
  }

  override def as(right: Path[T]) = number(getType, ALIAS, this, right)

  override def as(alias: String): NumberExpression[T] = as(new PathImpl[T](getType, alias))

}

object BooleanExpression {

  def allOf(expr: BooleanExpression*) = expr reduceLeft { _ && _ }

  def anyOf(expr: BooleanExpression*) = expr reduceLeft { _ || _ }

}

trait BooleanExpression extends ComparableExpression[java.lang.Boolean] with Predicate {

  def and(right: Predicate) = boolean(Ops.AND, this, right)

  def andAnyOf(expr: BooleanExpression*) = and(BooleanExpression.anyOf(expr:_*))

  def &&(right: Predicate) = and(right)

  def or(right: Predicate) = boolean(Ops.OR, this, right)

  def orAllOf(expr: BooleanExpression*) = or(BooleanExpression.allOf(expr:_*))

  def ||(right: Predicate) = or(right)

  def not() = boolean(Ops.NOT, this)

  def unary_! = not()

  override def as(right: Path[java.lang.Boolean]) = boolean(ALIAS.asInstanceOf[Operator[java.lang.Boolean]], this, right)

  override def as(alias: String): BooleanExpression = as(new PathImpl[java.lang.Boolean](getType, alias))

}

trait StringExpression extends ComparableExpression[String] {

  def like(right: String): BooleanExpression = like(constant(right))

  def like(right: Expression[String]) = boolean(Ops.LIKE, this, right)

  def append(right: Expression[String]) = string(Ops.CONCAT, this, right)

  def append(right: String): StringExpression = append(constant(right))

  def +(right: Expression[String]) = append(right)

  def +(right: String) = append(right)

  def concat(right: Expression[String]) = append(right)

  def concat(right: String) = append(right)

  def prepend(right: Expression[String]) = string(Ops.CONCAT, right, this)

  def prepend(right: String): StringExpression = prepend(constant(right))

  def stringValue() = this

  def lower = toLowerCase

  def upper = toUpperCase

  def matches(right: Expression[String]) = boolean(Ops.MATCHES, this, right)

  def matches(right: String): BooleanExpression = matches(constant(right))

  def indexOf(right: Expression[String]) = number[Int](classOf[Int], Ops.INDEX_OF, this, right)

  def indexOf(right: String): NumberExpression[Int] = indexOf(constant(right))

  def indexOf(left: String, right: Int): NumberExpression[Int] = indexOf(constant(left), right)

  def indexOf(left: Expression[String], right: Int) = {
    number[Int](classOf[Int], Ops.INDEX_OF_2ARGS, this, left, constant(right))
  }

  def charAt(right: Expression[Int]) = simple(classOf[Character], Ops.CHAR_AT, this, right)

  def charAt(right: Int): SimpleExpression[Character] = charAt(constant(right))

  def contains(right: Expression[String]) = boolean(Ops.STRING_CONTAINS, this, right)

  def contains(right: String): BooleanExpression = contains(constant(right))

  def endsWith(right: Expression[String]) = boolean(Ops.ENDS_WITH, this, right)

  def endsWith(right: String): BooleanExpression = endsWith(constant(right))

  def equalsIgnoreCase(right: Expression[String]) = boolean(Ops.EQ_IGNORE_CASE, this, right)

  def equalsIgnoreCase(right: String): BooleanExpression = equalsIgnoreCase(constant(right))

  lazy val isEmpty = boolean(Ops.STRING_IS_EMPTY, this)

  lazy val length = number[Int](classOf[Int], Ops.STRING_LENGTH, this)

  def startsWith(right: Expression[String]) = boolean(Ops.STARTS_WITH, this, right)

  def startsWith(right: String): BooleanExpression = startsWith(constant(right))

  def substring(right: Int) = string(Ops.SUBSTR_1ARG, this, constant(right))

  def substring(right: Int, arg1: Int) = string(Ops.SUBSTR_2ARGS, this, constant(right), constant(arg1))

  lazy val toLowerCase = string(Ops.LOWER, this)

  lazy val toUpperCase = string(Ops.UPPER, this)

  def trim() = string(Ops.TRIM, this)

  def containsIgnoreCase(right: Expression[String]) = boolean(Ops.STRING_CONTAINS_IC, this, right)

  def containsIgnoreCase(right: String): BooleanExpression = containsIgnoreCase(constant(right))

  def endsWithIgnoreCase(right: Expression[String]) = boolean(Ops.ENDS_WITH_IC, this, right)

  def endsWithIgnoreCase(right: String): BooleanExpression = endsWithIgnoreCase(constant(right))

  lazy val isNotEmpty = isEmpty.not

  def startsWithIgnoreCase(right: Expression[String]) = boolean(Ops.STARTS_WITH_IC, this, right)

  def startsWithIgnoreCase(right: String): BooleanExpression = startsWithIgnoreCase(constant(right))

  override def as(right: Path[String]) = string(ALIAS.asInstanceOf[Operator[String]], this, right)

  override def as(alias: String): StringExpression = as(new PathImpl[String](getType, alias))

}

trait TemporalExpression[T <: Comparable[_]] extends ComparableExpression[T] {

  def after(right: T) = gt(right)

  def after(right: Expression[T]) = gt(right)

  def before(right: T) = lt(right)

  def before(right: Expression[T]) = lt(right)

}

trait TimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

  lazy val hour = number(classOf[Integer], DateTimeOps.HOUR, this)

  lazy val minute = number(classOf[Integer], DateTimeOps.MINUTE, this)

  lazy val second = number(classOf[Integer], DateTimeOps.SECOND, this)

  lazy val milliSecond = number(classOf[Integer], DateTimeOps.MILLISECOND, this)

  override def as(right: Path[T]) = time(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): TimeExpression[T] = as(new PathImpl[T](getType, alias))

}

trait DateTimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

  lazy val min = dateTime(getType, AggOps.MIN_AGG, this)

  lazy val max = dateTime(getType, AggOps.MAX_AGG, this)

  lazy val dayOfMonth = number(classOf[Integer], DateTimeOps.DAY_OF_MONTH, this)

  lazy val dayOfWeek = number(classOf[Integer], DateTimeOps.DAY_OF_WEEK, this)

  lazy val dayOfYear = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this)

  lazy val week = number(classOf[Integer], DateTimeOps.WEEK, this)

  lazy val month = number(classOf[Integer], DateTimeOps.MONTH, this)

  lazy val year = number(classOf[Integer], DateTimeOps.YEAR, this)

  lazy val yearMonth = year.multiply(100).add(month)

  lazy val yearWeek = year.multiply(100).add(week)

  lazy val hour = number(classOf[Integer], DateTimeOps.HOUR, this)

  lazy val minute = number(classOf[Integer], DateTimeOps.MINUTE, this)

  lazy val second = number(classOf[Integer], DateTimeOps.SECOND, this)

  lazy val milliSecond = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this)

  override def as(right: Path[T]) = dateTime(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): DateTimeExpression[T] = as(new PathImpl[T](getType, alias))

}

trait DateExpression[T <: Comparable[_]] extends TemporalExpression[T] {

  lazy val min = date(getType, AggOps.MIN_AGG, this)

  lazy val max = date(getType, AggOps.MAX_AGG, this)

  lazy val dayOfMonth = number(classOf[Integer], DateTimeOps.DAY_OF_MONTH, this)

  lazy val dayOfWeek = number(classOf[Integer], DateTimeOps.DAY_OF_WEEK, this)

  lazy val dayOfYear = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this)

  lazy val week = number(classOf[Integer], DateTimeOps.WEEK, this)

  lazy val month = number(classOf[Integer], DateTimeOps.MONTH, this)

  lazy val year = number(classOf[Integer], DateTimeOps.YEAR, this)

  lazy val yearMonth = year.multiply(100).add(month)

  lazy val yearWeek = year.multiply(100).add(week)

  override def as(right: Path[T]) = date(getType, ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): DateExpression[T] = as(new PathImpl[T](getType, alias))

}

trait EnumExpression[T <: Enum[T]] extends ComparableExpression[T] {

  lazy val ordinal = number(classOf[Integer], Ops.ORDINAL, this)

  override def as(right: Path[T]) = enum(getType.asInstanceOf[Class[T]], ALIAS.asInstanceOf[Operator[T]], this, right)

  override def as(alias: String): EnumExpression[T] = as(new PathImpl[T](getType, alias))

  def mapToId[T <: { def id: java.lang.Long }](events: List[T]) = events groupBy (_.id.longValue) toList

}

