package com.mysema.query.scala;

import com.mysema.query.types._;
import java.util.Collection;

trait SimpleExpression[T] extends Expression[T] {

    def _count(): NumberExpression[Integer];

    def _in(right: Collection[T]): BooleanExpression;

    def _in(right: Array[T]): BooleanExpression;

    def _in(right: CollectionExpression[T]): BooleanExpression;

    def _eq(right: T): BooleanExpression;

    def _eq(right: Expression[T]): BooleanExpression;

    def _as(right: Path[T]): SimpleExpression[T];

    def _as(right: String): SimpleExpression[T];

    def _countDistinct(): NumberExpression[Long];

    def _isNotNull(): BooleanExpression;

    def _isNull(): BooleanExpression;

    def _ne(right: T): BooleanExpression;

    def _ne(right: Expression[T]): BooleanExpression;

    def _notIn(right: Collection[T]): BooleanExpression;

    def _notIn(right: Array[T]): BooleanExpression;

    def _notIn(right: CollectionExpression[T]): BooleanExpression;

}

trait CollectionExpression[T] extends SimpleExpression[T] {
    
}

trait ComparableExpression[T] extends SimpleExpression[T] {

    def _lt(right: T): BooleanExpression;

    def _lt(right: Expression[T]): BooleanExpression;

    def _as(right: Path[T]): ComparableExpression[T];

    def _as(right: String): ComparableExpression[T];

    def _between(left: T, right: T): BooleanExpression;

    def _between(left: Expression[T], right: Expression[T]): BooleanExpression;

    def _notBetween(left: T, right: T): BooleanExpression;

    def _notBetween(left: Expression[T], right: Expression[T]): BooleanExpression;

    def _gt(right: T): BooleanExpression;

    def _gt(right: Expression[T]): BooleanExpression;

    def _goe(right: T): BooleanExpression;

    def _goe(right: Expression[T]): BooleanExpression;

    def _loe(right: T): BooleanExpression;

    def _loe(right: Expression[T]): BooleanExpression;

}

trait NumberExpression[T] extends SimpleExpression[T] {

    def _add(right: Expression[Number]): NumberExpression[T];

    def _add(right: Number): NumberExpression[T];

    def _abs(): NumberExpression[T];

    def _sqrt(): NumberExpression[T];

    def _min(): NumberExpression[T];

    def _max(): NumberExpression[T];

    def _lt(right: Number): BooleanExpression;

    def _lt(right: Expression[Number]): BooleanExpression;

    def _in(right: Array[Number]): BooleanExpression;

    def _byteValue(): NumberExpression[Byte];

    def _doubleValue(): NumberExpression[Double];

    def _floatValue(): NumberExpression[Float];

    def _intValue(): NumberExpression[Integer];

    def _longValue(): NumberExpression[Long];

    def _shortValue(): NumberExpression[Short];

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

trait BooleanExpression extends ComparableExpression[Boolean] {

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

trait TemporalExpression[T] extends ComparableExpression[T] {

    def _after(right: T): BooleanExpression;

    def _after(right: Expression[T]): BooleanExpression;

    def _before(right: T): BooleanExpression;

    def _before(right: Expression[T]): BooleanExpression;

}

trait TimeExpression[T] extends TemporalExpression[T] {

    def _as(right: Path[T]): TimeExpression[T];

    def _as(right: String): TimeExpression[T];

    def _hour(): NumberExpression[Integer];

    def _minute(): NumberExpression[Integer];

    def _second(): NumberExpression[Integer];

    def _milliSecond(): NumberExpression[Integer];

}

trait DateTimeExpression[T] extends TemporalExpression[T] {

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

trait DateExpression[T] extends TemporalExpression[T] {

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

trait EnumExpression[T] extends ComparableExpression[T] {

    def _ordinal(): NumberExpression[Integer];

    def _as(right: Path[T]): EnumExpression[T];

    def _as(right: String): EnumExpression[T];

}


