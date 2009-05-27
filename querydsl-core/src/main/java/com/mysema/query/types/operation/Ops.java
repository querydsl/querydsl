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
    Operator<Boolean> EQ_PRIMITIVE = new Operator<Boolean>(Object_x_2);
    Operator<Boolean> EQ_OBJECT = new Operator<Boolean>(Object_x_2);
    Operator<Boolean> ISNOTNULL = new Operator<Boolean>(Object.class);
    Operator<Boolean> ISNULL = new Operator<Boolean>(Object.class);
    Operator<Boolean> ISTYPEOF = new Operator<Boolean>(Object.class, Class.class);
    Operator<Boolean> NE_PRIMITIVE = new Operator<Boolean>(Object_x_2);
    Operator<Boolean> NE_OBJECT = new Operator<Boolean>(Object_x_2);
    Operator<Number> NUMCAST = new Operator<Number>(Number.class, Class.class);
    Operator<String> STRING_CAST = new Operator<String>(Object.class);
    
    // collection
    Operator<Boolean> IN = new Operator<Boolean>(Object_x_2); // cmp. contains
    Operator<Boolean> NOTIN = new Operator<Boolean>(Object_x_2); // cmp. not contains
    Operator<Boolean> COL_ISEMPTY = new Operator<Boolean>(Object.class);
    Operator<Boolean> COL_ISNOTEMPTY = new Operator<Boolean>(Object.class);
    Operator<Number> COL_SIZE = new Operator<Number>(Object.class);

    // array
    Operator<Number> ARRAY_SIZE = new Operator<Number>(Object.class);
    
    // map
    Operator<Boolean> MAP_ISEMPTY = new Operator<Boolean>(Object.class);
    Operator<Boolean> MAP_ISNOTEMPTY = new Operator<Boolean>(Object.class);

    // Boolean
    Operator<Boolean> AND = new Operator<Boolean>(Boolean_x_2);
    Operator<Boolean> NOT = new Operator<Boolean>(Boolean.class);
    Operator<Boolean> OR = new Operator<Boolean>(Boolean_x_2);
    Operator<Boolean> XNOR = new Operator<Boolean>(Boolean_x_2);
    Operator<Boolean> XOR = new Operator<Boolean>(Boolean_x_2);

    // Comparable
    Operator<Boolean> BETWEEN = new Operator<Boolean>(Comparable_x_3);
    Operator<Boolean> GOE = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> GT = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> LOE = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> LT = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> NOTBETWEEN = new Operator<Boolean>(Comparable_x_3);
    
    // Date / Comparable
    Operator<Boolean> AFTER = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> BEFORE = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> AOE = new Operator<Boolean>(Comparable_x_2);
    Operator<Boolean> BOE = new Operator<Boolean>(Comparable_x_2);

    // Number
    Operator<Number> ADD = new Operator<Number>(Number_x_2);
    Operator<Number> DIV = new Operator<Number>(Number_x_2);
    Operator<Number> MOD = new Operator<Number>(Number_x_2);
    Operator<Number> MULT = new Operator<Number>(Number_x_2);
    Operator<Number> SUB = new Operator<Number>(Number_x_2);

    // String
    Operator<Character> CHAR_AT = new Operator<Character>(String.class, Integer.class);
    Operator<String> CONCAT = new Operator<String>(String_x_2);
    Operator<Boolean> LIKE = new Operator<Boolean>(String_x_2);
    Operator<String> LOWER = new Operator<String>(String.class);
    Operator<String> SUBSTR1ARG = new Operator<String>(String.class, Integer.class);
    Operator<String> SUBSTR2ARGS = new Operator<String>(String.class, Integer.class, Integer.class);
    Operator<String> SPLIT = new Operator<String>(String_x_2);
    Operator<String> TRIM = new Operator<String>(String.class);
    Operator<String> UPPER = new Operator<String>(String.class);
    Operator<Boolean> MATCHES = new Operator<Boolean>(String_x_2);
    Operator<Number> STRING_LENGTH = new Operator<Number>(String.class);
    Operator<Number> LAST_INDEX_2ARGS = new Operator<Number>(String.class, String.class, Integer.class);
    Operator<Number> LAST_INDEX = new Operator<Number>(String_x_2);
    Operator<Boolean> STRING_ISEMPTY = new Operator<Boolean>(String.class);
    Operator<Boolean> STARTSWITH = new Operator<Boolean>(String_x_2);
    Operator<Boolean> STARTSWITH_IC = new Operator<Boolean>(String_x_2);
    Operator<Number> INDEXOF_2ARGS = new Operator<Number>(String.class, String.class, Integer.class);
    Operator<Number> INDEXOF = new Operator<Number>(String.class, String.class);
    Operator<Boolean> EQ_IGNORECASE = new Operator<Boolean>(String_x_2);
    Operator<Boolean> ENDSWITH = new Operator<Boolean>(String_x_2);
    Operator<Boolean> ENDSWITH_IC = new Operator<Boolean>(String_x_2);
    Operator<Boolean> CONTAINS = new Operator<Boolean>(String_x_2);
    Operator<Boolean> CONTAINS_KEY = new Operator<Boolean>(Object_x_2);
    Operator<Boolean> CONTAINS_VALUE = new Operator<Boolean>(Object_x_2);

    // subquery operations
    Operator<Boolean> EXISTS = new Operator<Boolean>(Object.class);

    public static final List<Operator<?>> equalsOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE));

    public static final List<Operator<?>> notEqualsOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(NE_OBJECT, NE_PRIMITIVE));

    public static final List<Operator<?>> compareOps = Collections.unmodifiableList(Arrays.<Operator<?>> asList(EQ_OBJECT, EQ_PRIMITIVE,LT, GT, GOE, LOE));

    /**
     * Aggreate operators
     * 
     */
    public interface OpNumberAgg {
        Operator<Number> AVG_AGG = new Operator<Number>(Number.class);
        Operator<Number> MAX_AGG = new Operator<Number>(Number.class);
        Operator<Number> MIN_AGG = new Operator<Number>(Number.class);
    }

    /**
     * Date and time operators
     */
    public interface OpDateTime {
        Operator<java.util.Date> CURRENT_DATE = new Operator<java.util.Date>();
        Operator<java.util.Date> CURRENT_TIME = new Operator<java.util.Date>();
        Operator<java.util.Date> CURRENT_TIMESTAMP = new Operator<java.util.Date>();
        Operator<Integer> DAY = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> HOUR = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> MINUTE = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> MONTH = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> SECOND = new Operator<Integer>(java.util.Date.class);
        Operator<java.util.Date> SYSDATE = new Operator<java.util.Date>();
        Operator<Integer> YEAR = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> WEEK = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_WEEK = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_MONTH = new Operator<Integer>(java.util.Date.class);
        Operator<Integer> DAY_OF_YEAR = new Operator<Integer>(java.util.Date.class);
    }

    /**
     * Math operators
     * 
     */
    public interface OpMath {
        Operator<Number> ABS = new Operator<Number>(Number.class);
        Operator<Number> ACOS = new Operator<Number>(Number.class);
        Operator<Number> ASIN = new Operator<Number>(Number.class);
        Operator<Number> ATAN = new Operator<Number>(Number.class);
        Operator<Number> CEIL = new Operator<Number>(Number.class);
        Operator<Number> COS = new Operator<Number>(Number.class);
        Operator<Number> TAN = new Operator<Number>(Number.class);
        Operator<Number> SQRT = new Operator<Number>(Number.class);
        Operator<Number> SIN = new Operator<Number>(Number.class);
        Operator<Number> ROUND = new Operator<Number>(Number.class);
        Operator<Number> RANDOM = new Operator<Number>();
        Operator<Number> POWER = new Operator<Number>(Number_x_2);
        Operator<Number> MIN = new Operator<Number>(Number_x_2);
        Operator<Number> MAX = new Operator<Number>(Number_x_2);
        Operator<Number> LOG10 = new Operator<Number>(Number.class);
        Operator<Number> LOG = new Operator<Number>(Number.class);
        Operator<Number> FLOOR = new Operator<Number>(Number.class);
        Operator<Number> EXP = new Operator<Number>(Number.class);
    }

    /**
     * String operators
     */
    public interface OpString {
        Operator<String> LTRIM = new Operator<String>(String.class);
        Operator<String> RTRIM = new Operator<String>(String.class);
        Operator<String> SPACE = new Operator<String>(Integer.class);
    }
}
