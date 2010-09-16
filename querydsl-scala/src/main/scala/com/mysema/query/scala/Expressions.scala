/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;
import com.mysema.query.scala.Constants._
import com.mysema.query.scala.Operations._

import com.mysema.query.types._
import com.mysema.query.types.Ops._
import java.util.Collection
import java.util.Arrays._;

object Constants {
    
    //def constant(value: java.lang.Integer) = ConstantImpl.create(value.intValue);
    
    def constant(value: String) = ConstantImpl.create(value);
    
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

trait CollectionExpression[T] extends SimpleExpression[java.util.Collection[T]] {
    
    // TODO
    
}

trait SetExpression[T] extends SimpleExpression[java.util.Set[T]] {
    
    // TODO
    
}

trait ListExpression[T] extends SimpleExpression[java.util.List[T]] {
    
    // TODO
    
}

trait MapExpression[K,V] extends SimpleExpression[java.util.Map[K,V]] {
    
    // TODO
    
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

trait NumberExpression[T <: Number with Comparable[T]] extends ComparableExpressionBase[T] {

    def _add(right: Expression[Number]) = number[T](getType, ADD, this, right);

    def _add(right: Number) : NumberExpression[T] = _add(constant(right));

    def _abs() = number[T](getType, MathOps.ABS, this);

    def _sqrt() = number[java.lang.Double](classOf[java.lang.Double], MathOps.SQRT, this);

    def _min() = number[T](getType, AggOps.MIN_AGG, this);

    def _max() = number[T](getType, AggOps.MAX_AGG, this);

    def _lt(right: Number) : BooleanExpression = _lt(constant(right));

    def _lt(right: Expression[Number]) = boolean(Ops.LT, this, right);

    def _in(right: Array[Number]) = boolean(IN, this, constant(asList(right:_*)));

    def _byteValue() = castToNum(classOf[java.lang.Byte]);

    def _doubleValue() = castToNum(classOf[java.lang.Double]);

    def _floatValue() = castToNum(classOf[java.lang.Float]);

    def _intValue() = castToNum(classOf[java.lang.Integer]);

    def _longValue() = castToNum(classOf[java.lang.Long]);

    def _shortValue() = castToNum(classOf[java.lang.Short]);

    def _ceil() = number[T](getType, MathOps.CEIL, this);

    def _floor() = number[T](getType, MathOps.FLOOR, this);

    def _round() = number[T](getType, MathOps.ROUND, this);

//    override def _as(right: Path[T]) = number(getType, ALIAS.asInstanceOf[Operator[T]], this, right);

//    override def _as(alias: String): NumberExpression[T] = _as(new PathImpl[T](getType, alias));

    def _sum() = number[T](getType, AggOps.SUM_AGG, this);

    def _avg() = number[T](getType, AggOps.AVG_AGG, this);

    def _divide(right: Expression[Number]) = number[T](getType, Ops.MULT, this, right);

    def _divide(right: Number): NumberExpression[T] = _divide(constant(right));

    def _goe(right: Number): BooleanExpression = _goe(constant(right));

    def _goe(right: Expression[Number]) = boolean(Ops.GOE, this, right);

    def _gt(right: Number): BooleanExpression = _gt(constant(right));

    def _gt(right: Expression[Number]) = boolean(Ops.GT, this, right);

    def _between(left: Number, right: Number): BooleanExpression = _between(constant(left), constant(right));

    def _between(left: Expression[Number], right: Expression[Number]) = boolean(Ops.BETWEEN, this, left, right);

    def _notBetween(left: Number, right: Number): BooleanExpression = _between(left, right)._not();

    def _notBetween(left: Expression[Number], right: Expression[Number]) = _between(left, right)._not();

    def _loe(right: Number): BooleanExpression = _loe(constant(right));

    def _loe(right: Expression[Number]) = boolean(Ops.LOE, this, right);

    def _mod(right: Expression[Number]) = number[T](getType, Ops.MOD, this, right);

    def _mod(right: Number): NumberExpression[T] = _mod(constant(right));

    def _multiply(right: Expression[Number]) = number[T](getType, Ops.MULT, this, right);

    def _multiply(right: Number): NumberExpression[T] = _multiply(constant(right));

    def _negate() = _multiply(-1);

    def _subtract(right: Expression[Number]) = number[T](getType, Ops.SUB, this, right);

    def _subtract(right: Number): NumberExpression[T] = _subtract(constant(right));

    def _notIn(right: Array[Number]) = boolean(IN, this, constant(asList(right:_*)))._not();
    
    private def castToNum[A <: Number with Comparable[A]](t : Class[A]): NumberExpression[A] = {
        if (t.equals(getType)){
            this.asInstanceOf[NumberExpression[A]];
        }else{
            number[A](t, Ops.NUMCAST, this, constant(t));
        }
    }

}

trait BooleanExpression extends ComparableExpression[java.lang.Boolean] with Predicate {

    def _and(right: Predicate) = boolean(Ops.AND, this, right);

    def _or(right: Predicate) = boolean(Ops.OR, this, right);

    //override def _as(right: Path[java.lang.Boolean]) = boolean(ALIAS.asInstanceOf[Operator[java.lang.Boolean]], this, right);

    //override def _as(alias: String): BooleanExpression = _as(new PathImpl[java.lang.Boolean](getType, alias));

    def _not() = boolean(Ops.NOT, this);
    
    def not() = _not();

}

trait StringExpression extends ComparableExpression[String] {

    def _append(right: Expression[String]) = string(Ops.CONCAT, this, right);

    def _append(right: String): StringExpression = _append(constant(right));

    def _indexOf(right: Expression[String]) = number[Integer](classOf[Integer], Ops.INDEX_OF, this, right);

    def _indexOf(right: String): NumberExpression[Integer] = _indexOf(constant(right));

    def _indexOf(left: String, right: Int): NumberExpression[Integer] = _indexOf(constant(left), right);

    def _indexOf(left: Expression[String], right: Int) = number[Integer](classOf[Integer], Ops.INDEX_OF_2ARGS, this, left, constant(right));

    def _charAt(right: Expression[Integer]) = simple(classOf[Character], Ops.CHAR_AT, this, right);

    def _charAt(right: Integer): SimpleExpression[Character] = _charAt(constant(right));

    def _concat(right: Expression[String]) = _append(right);

    def _concat(right: String) = _append(right);

    def _contains(right: Expression[String]) = boolean(Ops.STRING_CONTAINS, this, right);

    def _contains(right: String) : BooleanExpression = _contains(constant(right));

    def _endsWith(right: Expression[String]) = boolean(Ops.ENDS_WITH, this, right);

    def _endsWith(right: String): BooleanExpression = _endsWith(constant(right));

    def _equalsIgnoreCase(right: Expression[String]) = boolean(Ops.EQ_IGNORE_CASE, this, right);

    def _equalsIgnoreCase(right: String): BooleanExpression = _equalsIgnoreCase(constant(right));

    def _isEmpty() = boolean(Ops.STRING_IS_EMPTY, this);

    def _length() = number[Integer](classOf[Integer], Ops.STRING_LENGTH, this);

    def _matches(right: Expression[String]) = boolean(Ops.MATCHES, this, right);

    def _matches(right: String): BooleanExpression = _matches(constant(right));

    def _startsWith(right: Expression[String]) = boolean(Ops.STARTS_WITH, this, right);

    def _startsWith(right: String) : BooleanExpression = _startsWith(constant(right));

    def _substring(right: Int) = string(Ops.SUBSTR_1ARG, this, constant(right));

    def _substring(right: Int, arg1: Int) = string(Ops.SUBSTR_2ARGS, this, constant(right), constant(arg1));

    def _toLowerCase() = string(Ops.LOWER);

    def _toUpperCase() = string(Ops.UPPER);

    def _trim() = string(Ops.TRIM);

    def _prepend(right: Expression[String]) = string(Ops.CONCAT, right, this);

    def _prepend(right: String) : StringExpression = _prepend(constant(right));

    //override def _as(right: Path[String]): StringExpression;

    //override def _as(right: String): StringExpression;

    def _stringValue() = this;

    def _lower() = _toLowerCase();

    def _upper() = _toUpperCase();

    def _containsIgnoreCase(right: Expression[String]) = boolean(Ops.STRING_CONTAINS_IC, this, right);

    def _containsIgnoreCase(right: String): BooleanExpression = _containsIgnoreCase(constant(right));

    def _endsWithIgnoreCase(right: Expression[String]) = boolean(Ops.ENDS_WITH_IC, this, right);

    def _endsWithIgnoreCase(right: String): BooleanExpression = _endsWithIgnoreCase(constant(right));

    def _isNotEmpty() = _isEmpty()._not();

    def _like(right: String): BooleanExpression = _like(constant(right));

    def _like(right: Expression[String]) = boolean(Ops.LIKE, this, right);

    def _startsWithIgnoreCase(right: Expression[String]) = boolean(Ops.STARTS_WITH_IC, this, right);

    def _startsWithIgnoreCase(right: String): BooleanExpression = _startsWithIgnoreCase(constant(right));

}

trait TemporalExpression[T <: Comparable[_]] extends ComparableExpression[T] {

    def _after(right: T) = _gt(right);

    def _after(right: Expression[T]) = _gt(right);

    def _before(right: T) = _lt(right);

    def _before(right: Expression[T]) = _lt(right);

}

trait TimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    //override def _as(right: Path[T]): TimeExpression[T];

    //override def _as(right: String): TimeExpression[T];

    def _hour() = number(classOf[Integer], DateTimeOps.HOUR, this);

    def _minute() = number(classOf[Integer], DateTimeOps.MINUTE, this);

    def _second() = number(classOf[Integer], DateTimeOps.SECOND, this);

    def _milliSecond() = number(classOf[Integer], DateTimeOps.MILLISECOND, this);

}

trait DateTimeExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    def _min() = dateTime(getType, AggOps.MIN_AGG, this);

    def _max() = dateTime(getType, AggOps.MAX_AGG, this);

    //override def _as(right: Path[T]): DateTimeExpression[T];

    //override def _as(right: String): DateTimeExpression[T];
    
    def _dayOfMonth() = number(classOf[Integer], DateTimeOps.DAY_OF_MONTH, this);

    def _dayOfWeek() = number(classOf[Integer], DateTimeOps.DAY_OF_WEEK, this);

    def _dayOfYear() = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this);

    def _week() = number(classOf[Integer], DateTimeOps.WEEK, this);

    def _month() = number(classOf[Integer], DateTimeOps.MONTH, this);

    def _year() = number(classOf[Integer], DateTimeOps.YEAR, this);

    def _yearMonth() = number(classOf[Integer], DateTimeOps.YEAR_MONTH, this);    

    def _hour() = number(classOf[Integer], DateTimeOps.HOUR, this);

    def _minute() = number(classOf[Integer], DateTimeOps.MINUTE, this);

    def _second() = number(classOf[Integer], DateTimeOps.SECOND, this);

    def _milliSecond() = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this);

}

trait DateExpression[T <: Comparable[_]] extends TemporalExpression[T] {

    def _min() = date(getType, AggOps.MIN_AGG, this);

    def _max() = date(getType, AggOps.MAX_AGG, this);

    //override def _as(right: Path[T]): DateExpression[T];

    //override def _as(right: String): DateExpression[T];

    def _dayOfMonth() = number(classOf[Integer], DateTimeOps.DAY_OF_MONTH, this);

    def _dayOfWeek() = number(classOf[Integer], DateTimeOps.DAY_OF_WEEK, this);

    def _dayOfYear() = number(classOf[Integer], DateTimeOps.DAY_OF_YEAR, this);

    def _week() = number(classOf[Integer], DateTimeOps.WEEK, this);

    def _month() = number(classOf[Integer], DateTimeOps.MONTH, this);

    def _year() = number(classOf[Integer], DateTimeOps.YEAR, this);

    def _yearMonth() = number(classOf[Integer], DateTimeOps.YEAR_MONTH, this);

}

trait EnumExpression[T <: Enum[T]] extends ComparableExpression[T] {

    def _ordinal() = number(classOf[Integer], Ops.ORDINAL, this);

    //override def _as(right: Path[T]): EnumExpression[T];

    //override def _as(right: String): EnumExpression[T];

}


