/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.types.Ops._;

import com.mysema.query.scala.Constants._;
import com.mysema.query.scala.Operations._;

import java.util.Collection;
import java.util.Arrays._;

object Constants {
    
    def constant[T](value: T) = new ConstantImpl(value);
    
}

trait SimpleExpression[T] extends Expression[T] {

    def _count() = number[java.lang.Long](classOf[java.lang.Long], AggOps.COUNT_AGG, this);

    def _in(right: Collection[T]) = boolean(IN, this, constant(right));

    def _in(right: T*) : BooleanOperation = _in(asList(right:_*));

    def _in(right: CollectionExpression[T]) = boolean(IN, this, right);

    def _eq(right: T): BooleanExpression = _eq(constant(right));

    def _eq(right: Expression[T]) = boolean(EQ_OBJECT, this, right);

    // TODO : get rid of asInstanceOf
    def _as(right: Path[T]): SimpleExpression[T] = simple(getType, ALIAS.asInstanceOf[Operator[T]], this, right);

    def _as(alias: String): SimpleExpression[T] = _as(new PathImpl[T](getType, alias)); 

    def _countDistinct() = number[java.lang.Long](classOf[java.lang.Long], AggOps.COUNT_DISTINCT_AGG, this);

    def _isNotNull() = boolean(IS_NOT_NULL, this);

    def _isNull() = boolean(IS_NULL, this);

    def _ne(right: T): BooleanExpression = _ne(constant(right));

    def _ne(right: Expression[T]) = boolean(NE_OBJECT, this, right);

    def _notIn(right: Collection[T]) = _in(right)._not();

    def _notIn(right: T*) = _in(right:_*)._not();

    def _notIn(right: CollectionExpression[T]) = _in(right)._not();

}

trait CollectionExpression[T] extends SimpleExpression[T] {
    
}

trait ComparableExpressionBase[T <: Comparable[_]] extends SimpleExpression[T] {
    
    def _asc = new OrderSpecifier[T](Order.ASC, this); 
    
    def _desc = new OrderSpecifier[T](Order.DESC, this);
    
}

trait ComparableExpression[T <: Comparable[_]] extends ComparableExpressionBase[T] {

    def _lt(right: T) : BooleanExpression = _lt(constant(right));

    def _lt(right: Expression[T]): BooleanExpression = boolean(BEFORE, this, right); 

    // TODO : get rid of asInstanceOf
    override def _as(right: Path[T]) = comparable(getType, ALIAS.asInstanceOf[Operator[T]], this, right);

    override def _as(alias: String): ComparableExpression[T] = _as(new PathImpl[T](getType, alias));

    def _between(left: T, right: T): BooleanExpression = _between(constant(left), constant(right));

    def _between(left: Expression[T], right: Expression[T]) = boolean(BETWEEN, this, left, right);;

    def _notBetween(left: T, right: T): BooleanExpression = _notBetween(constant(left), constant(right));

    def _notBetween(left: Expression[T], right: Expression[T]) = _between(left, right)._not();

    def _gt(right: T): BooleanExpression = _gt(constant(right));

    def _gt(right: Expression[T]) = boolean(AFTER, this, right);

    def _goe(right: T): BooleanExpression = _goe(constant(right));

    def _goe(right: Expression[T]) = boolean(AOE, this, right);

    def _loe(right: T): BooleanExpression = _loe(constant(right));

    def _loe(right: Expression[T]) = boolean(BOE, this, right);

}

trait NumberExpression[T <: Number with Comparable[T] ] extends ComparableExpressionBase[T] {

    // TODO : get rid of asInstanceOf
//    def _add(right: Expression[Number]) = number(getType, ADD, this, right);

//    def _add(right: Number) : NumberExpression[T] = _add(constant(right));

    def _abs(): NumberExpression[T];

    def _sqrt(): NumberExpression[T];

    def _min(): NumberExpression[T];

    def _max(): NumberExpression[T];

    def _lt(right: Number) : BooleanExpression = _lt(constant(right));

    def _lt(right: Expression[Number]): BooleanExpression;

    def _in(right: Array[Number]): BooleanExpression;

    def _byteValue(): NumberExpression[java.lang.Byte];

    def _doubleValue(): NumberExpression[java.lang.Double];

    def _floatValue(): NumberExpression[java.lang.Float];

    def _intValue(): NumberExpression[java.lang.Integer];

    def _longValue(): NumberExpression[java.lang.Long];

    def _shortValue(): NumberExpression[java.lang.Short];

    def _ceil(): NumberExpression[T];

    def _floor(): NumberExpression[T];

    def _random(): NumberExpression[T];

    def _round(): NumberExpression[T];

    def _as(right: Path[T]): NumberExpression[T];

    def _as(right: String): NumberExpression[T];

    def _sum(): NumberExpression[T];

    def _avg(): NumberExpression[T];

    def _divide(right: Expression[Number]): NumberExpression[T];

    def _divide(right: Number): NumberExpression[T];

    def _goe(right: Number): BooleanExpression;

    def _goe(right: Expression[Number]): BooleanExpression;

    def _gt(right: Number): BooleanExpression;

    def _gt(right: Expression[Number]): BooleanExpression;

    def _between(left: Number, right: Number): BooleanExpression;

    def _between(left: Expression[T], right: Expression[T]): BooleanExpression;

    def _notBetween(left: Number, right: Number): BooleanExpression;

    def _notBetween(left: Expression[T], right: Expression[T]): BooleanExpression;

    def _loe(right: Number): BooleanExpression;

    def _loe(right: Expression[Number]): BooleanExpression;

    def _mod(right: Expression[Number]): NumberExpression[T];

    def _mod(right: Number): NumberExpression[T];

    def _multiply(right: Expression[Number]): NumberExpression[T];

    def _multiply(right: Number): NumberExpression[T];

    def _negate(): NumberExpression[T];

    def _subtract(right: Expression[Number]): NumberExpression[T];

    def _subtract(right: Number): NumberExpression[T];

    def _notIn(right: Array[Number]): BooleanExpression;

    def _notIn(right: Array[Object]): BooleanExpression;

}

trait BooleanExpression extends ComparableExpression[java.lang.Boolean] {

    def _and(right: Predicate): BooleanExpression;

    def _or(right: Predicate): BooleanExpression;

    def _as(right: Path[Boolean]): BooleanExpression;

    def _as(right: String): BooleanExpression;

    def _not(): BooleanExpression;

}

trait StringExpression extends ComparableExpression[String] {

    def _append(right: Expression[String]): StringExpression;

    def _append(right: String): StringExpression;

    def _indexOf(right: Expression[String]): NumberExpression[Integer];

    def _indexOf(right: String): NumberExpression[Integer];

    def _indexOf(left: String, right: Int): NumberExpression[Integer];

    def _indexOf(left: Expression[String], right: Int): NumberExpression[Integer];

    def _charAt(right: Expression[String]): SimpleExpression[Character];

    def _charAt(right: Int): SimpleExpression[Character];

    def _concat(right: Expression[String]): StringExpression;

    def _concat(right: String): StringExpression;

    def _contains(right: Expression[String]): BooleanExpression;

    def _contains(right: String): BooleanExpression;

    def _endsWith(right: Expression[String]): BooleanExpression;

    def _endsWith(right: String): BooleanExpression;

    def _equalsIgnoreCase(right: Expression[String]): BooleanExpression;

    def _equalsIgnoreCase(right: String): BooleanExpression;

    def _isEmpty(): BooleanExpression;

    def _length(): NumberExpression[Integer];

    def _matches(right: Expression[String]): BooleanExpression;

    def _matches(right: String): BooleanExpression;

    def _startsWith(right: Expression[String]): BooleanExpression;

    def _startsWith(right: String): BooleanExpression;

    def _substring(right: Int): StringExpression;

    def _substring(right: Int, arg1: Int): StringExpression;

    def _toLowerCase(): StringExpression;

    def _toUpperCase(): StringExpression;

    def _trim(): StringExpression;

    def _prepend(right: Expression[String]): StringExpression;

    def _prepend(right: String): StringExpression;

    def _as(right: Path[String]): StringExpression;

    def _as(right: String): StringExpression;

    def _stringValue(): StringExpression;

    def _lower(): StringExpression;

    def _upper(): StringExpression;

    def _containsIgnoreCase(right: Expression[String]): BooleanExpression;

    def _containsIgnoreCase(right: String): BooleanExpression;

    def _endsWithIgnoreCase(right: Expression[String]): BooleanExpression;

    def _endsWithIgnoreCase(right: String): BooleanExpression;

    def _isNotEmpty(): BooleanExpression;

    def _like(right: String): BooleanExpression;

    def _like(right: Expression[String]): BooleanExpression;

    def _startsWithIgnoreCase(right: Expression[String]): BooleanExpression;

    def _startsWithIgnoreCase(right: String): BooleanExpression;

}

trait TemporalExpression[T <: Comparable[_]] extends ComparableExpression[T] {

    def _after(right: T): BooleanExpression;

    def _after(right: Expression[T]): BooleanExpression;

    def _before(right: T): BooleanExpression;

    def _before(right: Expression[T]): BooleanExpression;

}

trait TimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    def _as(right: Path[T]): TimeExpression[T];

    def _as(right: String): TimeExpression[T];

    def _hour(): NumberExpression[Integer];

    def _minute(): NumberExpression[Integer];

    def _second(): NumberExpression[Integer];

    def _milliSecond(): NumberExpression[Integer];

}

trait DateTimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    def _min(): DateTimeExpression[T];

    def _max(): DateTimeExpression[T];

    def _as(right: Path[T]): DateTimeExpression[T];

    def _as(right: String): DateTimeExpression[T];
    
    def _dayOfMonth(): NumberExpression[Integer];

    def _dayOfWeek(): NumberExpression[Integer];

    def _dayOfYear(): NumberExpression[Integer];

    def _week(): NumberExpression[Integer];

    def _month(): NumberExpression[Integer];

    def _year(): NumberExpression[Integer];

    def _yearMonth(): NumberExpression[Integer];    

    def _hour(): NumberExpression[Integer];

    def _minute(): NumberExpression[Integer];

    def _second(): NumberExpression[Integer];

    def _milliSecond(): NumberExpression[Integer];

}

trait DateExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    def _min(): DateExpression[T];

    def _max(): DateExpression[T];

    def _as(right: Path[T]): DateExpression[T];

    def _as(right: String): DateExpression[T];

    def _dayOfMonth(): NumberExpression[Integer];

    def _dayOfWeek(): NumberExpression[Integer];

    def _dayOfYear(): NumberExpression[Integer];

    def _week(): NumberExpression[Integer];

    def _month(): NumberExpression[Integer];

    def _year(): NumberExpression[Integer];

    def _yearMonth(): NumberExpression[Integer];

}

trait EnumExpression[T <: Enum[T]] extends ComparableExpression[T] {

    def _ordinal() = number(classOf[Integer]

    def _as(right: Path[T]): EnumExpression[T];

    def _as(right: String): EnumExpression[T];

}


