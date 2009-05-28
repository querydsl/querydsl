/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ops provides the operators for the fluent query grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Ops {
    
    static List<Class<?>> Boolean_x_2 = unmodifiableList(Arrays.<Class<?>> asList(Boolean.class, Boolean.class));

    static List<Class<?>> Comparable_x_2 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class));

    static List<Class<?>> Comparable_x_3 = unmodifiableList(Arrays.<Class<?>> asList(Comparable.class, Comparable.class,Comparable.class));

    static List<Class<?>> Object_x_2 = unmodifiableList(Arrays.<Class<?>> asList(Object.class, Object.class));

    static List<Class<?>> Number_x_2 = unmodifiableList(Arrays.<Class<?>> asList(Number.class, Number.class));

    static List<Class<?>> String_x_2 = unmodifiableList(Arrays.<Class<?>> asList(String.class, String.class));

    // general
    OperatorImpl<Boolean> EQ_PRIMITIVE = new OperatorImpl<Boolean>(Object_x_2);
    OperatorImpl<Boolean> EQ_OBJECT = new OperatorImpl<Boolean>(Object_x_2);
    OperatorImpl<Boolean> ISNOTNULL = new OperatorImpl<Boolean>(Object.class);
    OperatorImpl<Boolean> ISNULL = new OperatorImpl<Boolean>(Object.class);
    OperatorImpl<Boolean> ISTYPEOF = new OperatorImpl<Boolean>(Object.class, Class.class);
    OperatorImpl<Boolean> NE_PRIMITIVE = new OperatorImpl<Boolean>(Object_x_2);
    OperatorImpl<Boolean> NE_OBJECT = new OperatorImpl<Boolean>(Object_x_2);
    OperatorImpl<Number> NUMCAST = new OperatorImpl<Number>(Number.class, Class.class);
    OperatorImpl<String> STRING_CAST = new OperatorImpl<String>(Object.class);
    
    // collection
    OperatorImpl<Boolean> IN = new OperatorImpl<Boolean>(Object_x_2); // cmp. contains
    OperatorImpl<Boolean> NOTIN = new OperatorImpl<Boolean>(Object_x_2); // cmp. not contains
    OperatorImpl<Boolean> COL_ISEMPTY = new OperatorImpl<Boolean>(Object.class);
    OperatorImpl<Boolean> COL_ISNOTEMPTY = new OperatorImpl<Boolean>(Object.class);
    OperatorImpl<Number> COL_SIZE = new OperatorImpl<Number>(Object.class);

    // array
    OperatorImpl<Number> ARRAY_SIZE = new OperatorImpl<Number>(Object.class);
    
    // map
    OperatorImpl<Boolean> MAP_ISEMPTY = new OperatorImpl<Boolean>(Object.class);
    OperatorImpl<Boolean> MAP_ISNOTEMPTY = new OperatorImpl<Boolean>(Object.class);

    // Boolean
    OperatorImpl<Boolean> AND = new OperatorImpl<Boolean>(Boolean_x_2);
    OperatorImpl<Boolean> NOT = new OperatorImpl<Boolean>(Boolean.class);
    OperatorImpl<Boolean> OR = new OperatorImpl<Boolean>(Boolean_x_2);
    OperatorImpl<Boolean> XNOR = new OperatorImpl<Boolean>(Boolean_x_2);
    OperatorImpl<Boolean> XOR = new OperatorImpl<Boolean>(Boolean_x_2);

    // Comparable
    OperatorImpl<Boolean> BETWEEN = new OperatorImpl<Boolean>(Comparable_x_3);
    OperatorImpl<Boolean> GOE = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> GT = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> LOE = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> LT = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> NOTBETWEEN = new OperatorImpl<Boolean>(Comparable_x_3);
    
    // Date / Comparable
    OperatorImpl<Boolean> AFTER = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> BEFORE = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> AOE = new OperatorImpl<Boolean>(Comparable_x_2);
    OperatorImpl<Boolean> BOE = new OperatorImpl<Boolean>(Comparable_x_2);

    // Number
    OperatorImpl<Number> ADD = new OperatorImpl<Number>(Number_x_2);
    OperatorImpl<Number> DIV = new OperatorImpl<Number>(Number_x_2);
    OperatorImpl<Number> MOD = new OperatorImpl<Number>(Number_x_2);
    OperatorImpl<Number> MULT = new OperatorImpl<Number>(Number_x_2);
    OperatorImpl<Number> SUB = new OperatorImpl<Number>(Number_x_2);

    // String
    OperatorImpl<Character> CHAR_AT = new OperatorImpl<Character>(String.class, Integer.class);
    OperatorImpl<String> CONCAT = new OperatorImpl<String>(String_x_2);
    OperatorImpl<Boolean> LIKE = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<String> LOWER = new OperatorImpl<String>(String.class);
    OperatorImpl<String> SUBSTR1ARG = new OperatorImpl<String>(String.class, Integer.class);
    OperatorImpl<String> SUBSTR2ARGS = new OperatorImpl<String>(String.class, Integer.class, Integer.class);
    OperatorImpl<String> SPLIT = new OperatorImpl<String>(String_x_2);
    OperatorImpl<String> TRIM = new OperatorImpl<String>(String.class);
    OperatorImpl<String> UPPER = new OperatorImpl<String>(String.class);
    OperatorImpl<Boolean> MATCHES = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Number> STRING_LENGTH = new OperatorImpl<Number>(String.class);
    OperatorImpl<Number> LAST_INDEX_2ARGS = new OperatorImpl<Number>(String.class, String.class, Integer.class);
    OperatorImpl<Number> LAST_INDEX = new OperatorImpl<Number>(String_x_2);
    OperatorImpl<Boolean> STRING_ISEMPTY = new OperatorImpl<Boolean>(String.class);
    OperatorImpl<Boolean> STARTSWITH = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Boolean> STARTSWITH_IC = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Number> INDEXOF_2ARGS = new OperatorImpl<Number>(String.class, String.class, Integer.class);
    OperatorImpl<Number> INDEXOF = new OperatorImpl<Number>(String.class, String.class);
    OperatorImpl<Boolean> EQ_IGNORECASE = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Boolean> ENDSWITH = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Boolean> ENDSWITH_IC = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Boolean> CONTAINS = new OperatorImpl<Boolean>(String_x_2);
    OperatorImpl<Boolean> CONTAINS_KEY = new OperatorImpl<Boolean>(Object_x_2);
    OperatorImpl<Boolean> CONTAINS_VALUE = new OperatorImpl<Boolean>(Object_x_2);
    
    // aggregation
    OperatorImpl<Number> AVG_AGG = new OperatorImpl<Number>(Number.class);
    OperatorImpl<Number> MAX_AGG = new OperatorImpl<Number>(Number.class);
    OperatorImpl<Number> MIN_AGG = new OperatorImpl<Number>(Number.class);
    OperatorImpl<Number> SUM_AGG = new OperatorImpl<Number>(Number.class);
    OperatorImpl<Number> COUNT_AGG = new OperatorImpl<Number>(Object.class);
    OperatorImpl<Number> COUNT_ALL_AGG = new OperatorImpl<Number>();

    // subquery operations
    OperatorImpl<Boolean> EXISTS = new OperatorImpl<Boolean>(Object.class);

    public static final List<Operator<?>> equalsOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE));

    public static final List<Operator<?>> notEqualsOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(NE_OBJECT, NE_PRIMITIVE));

    public static final List<Operator<?>> compareOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE,LT, GT, GOE, LOE));

    /**
     * Date and time operators
     */
    public interface DateTimeOps {
        OperatorImpl<java.util.Date> CURRENT_DATE = new OperatorImpl<java.util.Date>();
        OperatorImpl<java.util.Date> CURRENT_TIME = new OperatorImpl<java.util.Date>();
        OperatorImpl<java.util.Date> CURRENT_TIMESTAMP = new OperatorImpl<java.util.Date>();
        OperatorImpl<Integer> DAY = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> HOUR = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> MINUTE = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> MONTH = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> SECOND = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<java.util.Date> SYSDATE = new OperatorImpl<java.util.Date>();
        OperatorImpl<Integer> YEAR = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> WEEK = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> DAY_OF_WEEK = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> DAY_OF_MONTH = new OperatorImpl<Integer>(java.util.Date.class);
        OperatorImpl<Integer> DAY_OF_YEAR = new OperatorImpl<Integer>(java.util.Date.class);
    }

    /**
     * Math operators
     * 
     */
    public interface MathOps {
        OperatorImpl<Number> ABS = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> ACOS = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> ASIN = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> ATAN = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> CEIL = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> COS = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> TAN = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> SQRT = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> SIN = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> ROUND = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> RANDOM = new OperatorImpl<Number>();
        OperatorImpl<Number> POWER = new OperatorImpl<Number>(Number_x_2);
        OperatorImpl<Number> MIN = new OperatorImpl<Number>(Number_x_2);
        OperatorImpl<Number> MAX = new OperatorImpl<Number>(Number_x_2);
        OperatorImpl<Number> LOG10 = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> LOG = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> FLOOR = new OperatorImpl<Number>(Number.class);
        OperatorImpl<Number> EXP = new OperatorImpl<Number>(Number.class);
    }

    /**
     * String operators
     */
    public interface StringOps {
        OperatorImpl<String> LTRIM = new OperatorImpl<String>(String.class);
        OperatorImpl<String> RTRIM = new OperatorImpl<String>(String.class);
        OperatorImpl<String> SPACE = new OperatorImpl<String>(Integer.class);
    }
}
