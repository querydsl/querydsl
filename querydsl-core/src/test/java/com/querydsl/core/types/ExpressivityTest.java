package com.querydsl.core.types;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.dsl.*;

public class ExpressivityTest {
    
    private NumberExpression<Integer> num;
    
    private StringExpression str;
    
    private DateExpression<Date> date;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Before
    public void setUp() {
        num = Expressions.numberPath(Integer.class, "num");
        str = Expressions.stringPath("str");
        date = Expressions.datePath(Date.class, "date");
        QueryMixin<?> query = new QueryMixin<Void>();
        query.from(num, str);
        // TODO sub
    }
    
    @Test
    public void test() {
        //Field<T>               abs()
        num.abs();
        //Field<BigDecimal>      acos()
        MathExpressions.acos(num);        
        //Field<T>               add(Field<?> value)        
        num.add(num);
        //Field<T>               add(Number value)
        num.add(1);
        //Field<T>               as(String alias)
        num.as("other");
        //SortField<T>           asc()
        num.asc();
        //Field<Integer>         ascii()
        
        //Field<BigDecimal>      asin()
        MathExpressions.asin(num);
        //Field<BigDecimal>      atan()
        MathExpressions.atan(num);
        //Field<BigDecimal>      atan2(Field<? extends Number> y)
    
        //Field<BigDecimal>      atan2(Number y)

        //Field<BigDecimal>      avg()
        num.avg();
        //WindowPartitionByStep<BigDecimal>      avgOver()

        //Condition              between(Field<T> minValue, Field<T> maxValue)
        num.between(num, num);
        //Condition              between(T minValue, T maxValue)
        num.between(1,10);
        // bitAnd        
        
        //Field<Integer>         bitLength()
        
        // bitNand
        
        // bitNor
        
        // bitOr
        
        // bitXnor
        
        // bitXor

        //<Z> Field<Z>           cast(Class<? extends Z> type)
        num.castToNum(Long.class);        
        //<Z> Field<Z>           ast(DataType<Z> type)

        //<Z> Field<Z>           cast(Field<Z> field)

        //Field<T>               ceil()
        num.ceil();
        //Field<Integer>         charLength()
        str.length();
        //Field<T>               coalesce(Field<T> option, Field<?>... options)
        str.coalesce(str);
        //Field<T>               coalesce(T option, T... options)

        //Field<String>          concat(Field<?>... fields)
        str.concat(str);
        //Field<String>          concat(String... values)
        str.concat("str");
        //Condition              contains(Field<T> value)
        str.contains(str);
        //Condition              contains(T value)
        str.contains("a");
        //Field<BigDecimal>      cos()
        MathExpressions.cos(num);
        //Field<BigDecimal>      cosh()
        MathExpressions.cosh(num);
        //Field<BigDecimal>      cot()
        MathExpressions.cot(num);
        //Field<BigDecimal>      coth()
        MathExpressions.coth(num);
        //Field<Integer>         count()
        num.count();
        //Field<Integer>         countDistinct()
        num.countDistinct();
        //WindowPartitionByStep<Integer>         countOver()

        // currentDate
        Expressions.currentDate();
        // currentTime
        Expressions.currentTime();
        // currentTimestamp
        Expressions.currentTimestamp();
        // currval
        
        // dateadd
        
        // dateDiff
        // TODO
        //<Z> Field<Z>           decode(Field<T> search, Field<Z> result)

        //<Z> Field<Z>           decode(Field<T> search, Field<Z> result, Field<?>... more)

        //<Z> Field<Z>           decode(T search, Z result)

        //<Z> Field<Z>           decode(T search, Z result, Object... more)

        //Field<BigDecimal>      deg() 
        MathExpressions.degrees(num);
        //SortField<T>           desc()
        num.desc();        
        //Field<T>               div(Field<? extends Number> value)
        num.divide(num);
        //Field<T>               div(Number value)
        num.divide(2);
        //Condition              endsWith(Field<T> value)
        str.endsWith(str);
        //Condition              endsWith(T value)
        str.endsWith("str");
        //Condition              equal(Field<T> field)
        num.eq(1);
        //Condition              equal(Select<?> query)
        //num.eq(sub.unique(num));
        //Condition              equal(T value)
        num.eq(num);
        //Condition              equalAll(Field<T[]> array)

        //Condition              equalAll(Select<?> query)
        //num.eqAll(sub.select(num));
        //Condition              equalAll(T... array)

        //Condition              equalAny(Field<T[]> array) -> in
        
        //Condition              equalAny(Select<?> query) -> in
        //num.eqAny(sub.select(num));
        //Condition              equalAny(T... array) -> in
        
        //Condition              equalIgnoreCase(Field<String> value)
        str.equalsIgnoreCase(str);
        //Condition              equalIgnoreCase(String value)
        str.equalsIgnoreCase("abc");
        //Field<BigDecimal>      exp()
        MathExpressions.exp(num);
        //Field<Integer>         extract(DatePart datePart)
        date.month(); // ...
        //WindowIgnoreNullsStep<T>       firstValue()

        //Field<T>               floor()
        num.floor();
        //String                 getName()

        //Class<? extends T>     getType()
        num.getType();
        //Condition              greaterOrEqual(Field<T> field)
        num.goe(num);
        //Condition              greaterOrEqual(Select<?> query)
        //num.goe(sub.unique(num));
        //Condition              greaterOrEqual(T value)
        num.goe(1);
        //Condition              greaterOrEqualAll(Field<T[]> array)

        //Condition              greaterOrEqualAll(Select<?> query)
        //num.goeAll(sub.select(num));
        //Condition              greaterOrEqualAll(T... array)

        //Condition              greaterOrEqualAny(Field<T[]> array)

        //Condition              greaterOrEqualAny(Select<?> query)
        //num.goeAny(sub.select(num));
        //Condition              greaterOrEqualAny(T... array)

        //Condition              greaterThan(Field<T> field)
        num.gt(num);
        //Condition              greaterThan(Select<?> query)
        //num.gt(sub.select(num));
        //Condition              greaterThan(T value)
        num.gt(1);
        //Condition              greaterThanAll(Field<T[]> array)

        //Condition              greaterThanAll(Select<?> query)
        //num.gtAll(sub.select(num));
        //Condition              greaterThanAll(T... array)

        //Condition              greaterThanAny(Field<T[]> array)

        //Condition              greaterThanAny(Select<?> query)
        //num.gtAny(sub.select(num));
        //Condition              greaterThanAny(T... array)

        //Field<T>               greatest(Field<?>... others)
        MathExpressions.max(num, num);
        //Field<T>               greatest(T... others)

        //Condition              in(Collection<T> values)
        num.in(Arrays.asList(1,2,3));
        //Condition              in(Field<?>... values)
        
        //Condition              in(Select<?> query)
        //num.in(sub.select(num));
        //Condition              in(T... values)
        num.in(1,2,3);
        //Condition              isFalse()

        //Condition              isNotNull()
        num.isNotNull();
        //Condition              isNull()
        num.isNull();
        //boolean                isNullLiteral()

        //Condition              isTrue()

        //WindowIgnoreNullsStep<T>       lag()

        //WindowIgnoreNullsStep<T>       lag(int offset)

        //WindowIgnoreNullsStep<T>       lag(int offset, Field<T> defaultValue)

        //WindowIgnoreNullsStep<T>       lag(int offset, T defaultValue)

        //WindowIgnoreNullsStep<T>       lastValue()

        //WindowIgnoreNullsStep<T>       lead()

        //WindowIgnoreNullsStep<T>       lead(int offset)

        //WindowIgnoreNullsStep<T>       lead(int offset, Field<T> defaultValue)

        //WindowIgnoreNullsStep<T>       lead(int offset, T defaultValue)

        //Field<T>               least(Field<?>... others)
        MathExpressions.min(num, num);
        //Field<T>               least(T... others)

        //Field<Integer>         length()
        str.length();
        //Condition              lessOrEqual(Field<T> field)
        num.loe(num);
        //Condition              lessOrEqual(Select<?> query)
        //num.loe(sub.unique(num));
        //Condition              lessOrEqual(T value)
        num.loe(1);
        //Condition              lessOrEqualAll(Field<T[]> array)

        //Condition              lessOrEqualAll(Select<?> query)
        //num.loeAll(sub.select(num));
        //Condition              lessOrEqualAll(T... array)

        //Condition              lessOrEqualAny(Field<T[]> array)

        //Condition              lessOrEqualAny(Select<?> query)
        //num.loeAny(sub.select(num));
        //Condition              lessOrEqualAny(T... array)

        //Condition              lessThan(Field<T> field)
        num.lt(num);
        //Condition              lessThan(Select<?> query)
        //num.lt(sub.select(num));
        //Condition              lessThan(T value)
        num.lt(1);
        //Condition              lessThanAll(Field<T[]> array)

        //Condition              lessThanAll(Select<?> query)
        //num.ltAll(sub.select(num));
        //Condition              lessThanAll(T... array)

        //Condition              lessThanAny(Field<T[]> array)

        //Condition              lessThanAny(Select<?> query)
        //num.ltAny(sub.select(num));
        //Condition              lessThanAny(T... array)

        //Condition              like(Field<String> value)
        str.like(str);
        //Condition              like(Field<String> value, char escape)
        str.like(str, '!');
        //Condition              like(String value)
        str.like("a%");
        //Condition              like(String value, char escape)
        str.like("a%", '!');
        //Field<BigDecimal>      ln()
        MathExpressions.ln(num);
        //Field<BigDecimal>      log(int base)
        MathExpressions.log(num, 5);
        //Field<String>          lower()
        str.lower();
        //Field<String>          lpad(Field<? extends Number> length)
        StringExpressions.lpad(str, num);
        //Field<String>          lpad(Field<? extends Number> length, Field<String> character)
        StringExpressions.lpad(str, num, '!');
        //Field<String>          lpad(int length)
        StringExpressions.lpad(str, 10);
        //Field<String>          lpad(int length, char character)
        StringExpressions.lpad(str, 10, '!');
        //Field<String>          ltrim()
        StringExpressions.ltrim(str);
        //Field<T>               max()
        num.max();
        //WindowPartitionByStep<T>       maxOver()

        //Field<BigDecimal>      median()

        //Field<T>               min()
        num.min();
        //WindowPartitionByStep<T>       minOver()

        //Field<T>               mod(Field<? extends Number> value)
        num.mod(num);
        //Field<T>               mod(Number value)
        num.mod(10);
        //Field<T>               mul(Field<? extends Number> value)
        num.multiply(num);
        //Field<T>               mul(Number value)
        num.multiply(2);
        //Field<T>               neg()
        num.negate();
        //Condition              notBetween(Field<T> minValue, Field<T> maxValue)
        num.notBetween(num, num);
        //Condition              notBetween(T minValue, T maxValue)
        num.notBetween(1,10);
        //Condition              notEqual(Field<T> field)
        num.ne(num);
        //Condition              notEqual(Select<?> query)
        //num.ne(sub.select(num));
        //Condition              notEqual(T value)
        num.ne(1);
        //Condition              notEqualAll(Field<T[]> array)

        //Condition              notEqualAll(Select<?> query)
        //num.neAll(sub.select(num));
        //Condition              notEqualAll(T... array)

        //Condition              notEqualAny(Field<T[]> array) -> notIn

        //Condition              notEqualAny(Select<?> query) -> notIn
        //num.neAny(sub.select(num));
        //Condition              notEqualAny(T... array) -> notIn

        //Condition              notEqualIgnoreCase(Field<String> value)
        str.notEqualsIgnoreCase(str);
        //Condition              notEqualIgnoreCase(String value)
        str.notEqualsIgnoreCase("abc");
        //Condition              notIn(Collection<T> values)
        num.notIn(Arrays.asList(1,2,3));
        //Condition              notIn(Field<?>... values)
        
        //Condition              notIn(Select<?> query)
        //num.notIn(sub.select(num));
        //Condition              notIn(T... values)
        num.notIn(1, 2, 3);
        //Condition              notLike(Field<String> value)
        str.notLike(str);
        //Condition              notLike(Field<String> value, char escape)
        str.notLike(str, '!');
        //Condition              notLike(String value)
        str.notLike("a%");
        //Condition              notLike(String value, char escape)
        str.notLike("a%",'!');
        //Field<T>               nullif(Field<T> other)
        
        //Field<T>               nullif(T other)

        //Field<T>               nvl(Field<T> defaultValue)
        str.coalesce(str);
        //Field<T>               nvl(T defaultValue)
        str.coalesce("abc");
        //<Z> Field<Z>           nvl2(Field<Z> valueIfNotNull, Field<Z> valueIfNull)

        //<Z> Field<Z>           nvl2(Z valueIfNotNull, Z valueIfNull)

        //Field<Integer>         octetLength()

        //Field<Integer>         position(Field<String> search)
        str.locate(str);        
        //Field<Integer>         position(String search)
        str.locate("a");
        //Field<BigDecimal>      power(Number exponent)
        MathExpressions.power(num, 4);
        //Field<BigDecimal>      rad() 
        MathExpressions.radians(num);
        //Field<String>          repeat(Field<? extends Number> count)

        //Field<String>          repeat(Number count)

        //Field<String>          replace(Field<String> search)

        //Field<String>          replace(Field<String> search, Field<String> replace)

        //Field<String>          replace(String search)

        //Field<String>          replace(String search, String replace)

        //Field<T>               round()
        num.round();
        //Field<T>               round(int decimals)

        //Field<String>          rpad(Field<? extends Number> length)
        StringExpressions.rpad(str, num);
        //Field<String>          rpad(Field<? extends Number> length, Field<String> character)
        StringExpressions.rpad(str, num, '!');
        //Field<String>          rpad(int length)
        StringExpressions.rpad(str, 10);
        //Field<String>          rpad(int length, char character)
        StringExpressions.rpad(str, 10, '!');
        //Field<String>          rtrim()
        StringExpressions.rtrim(str);
        // shl (bitwise shift left)
        
        // shr (bitwise shift right)
        
        //Field<Integer>         sign()
        MathExpressions.sign(num);
        //Field<BigDecimal>      sin()
        MathExpressions.sin(num);
        //Field<BigDecimal>      sinh()
        MathExpressions.sinh(num);
        //<Z> SortField<Z>       sort(Map<T,Z> sortMap)

        //SortField<Integer>     sortAsc(Collection<T> sortList)

        //SortField<Integer>     sortAsc(T... sortList)

        //SortField<Integer>     sortDesc(Collection<T> sortList)

        //SortField<Integer>     sortDesc(T... sortList)

        //Field<BigDecimal>      sqrt()
        num.sqrt();
        //Condition              startsWith(Field<T> value)
        str.startsWith(str);
        //Condition              startsWith(T value)
        str.startsWith("a");
        //Field<BigDecimal>      stddevPop()

        //WindowPartitionByStep<BigDecimal>      stddevPopOver()

        //Field<BigDecimal>      stddevSamp()

        //WindowPartitionByStep<BigDecimal>      stddevSampOver()

        //Field<T>               sub(Field<?> value)
        num.subtract(num);
        //Field<T>               sub(Number value)
        num.subtract(1);
        //Field<String>          substring(Field<? extends Number> startingPosition)
        str.substring(num);
        //Field<String>          substring(Field<? extends Number> startingPosition, Field<? extends Number> length)
        str.substring(num, num);
        //Field<String>          substring(int startingPosition)
        str.substring(1);
        //Field<String>          substring(int startingPosition, int length)
        str.substring(1, 3);
        //Field<BigDecimal>      sum()
        num.sum();
        //WindowPartitionByStep<BigDecimal>      sumOver()

        //Field<BigDecimal>      tan()
        MathExpressions.tan(num);
        //Field<BigDecimal>      tanh()
        MathExpressions.tanh(num);
        //Field<String>          trim()
        str.trim();
        //Field<String>          upper()
        str.upper();
        //Field<BigDecimal>      varPop()

        //WindowPartitionByStep<BigDecimal>      varPopOver()

        //Field<BigDecimal>      varSamp()

        //WindowPartitionByStep<BigDecimal>      varSampOver()

    }

}
