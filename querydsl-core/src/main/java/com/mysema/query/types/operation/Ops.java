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

	/**
	 * The Class Op.
	 */
	public static class Op<RT> {
		private final List<Class<?>> types;

		public Op(Class<?> type) {
			this(Collections.<Class<?>> singletonList(type));
		}

		public Op(Class<?>... types) {
			this(Arrays.<Class<?>> asList(types));
		}

		public Op(List<Class<?>> types) {
			this.types = unmodifiableList(types);
		}

		public List<Class<?>> getTypes() {
			return types;
		}
	}

	// general
	Op<Boolean> EQ_PRIMITIVE = new Op<Boolean>(Object_x_2);
	Op<Boolean> EQ_OBJECT = new Op<Boolean>(Object_x_2);
	Op<Boolean> IN = new Op<Boolean>(Object_x_2);
	Op<Boolean> ISNOTNULL = new Op<Boolean>(Object.class);
	Op<Boolean> ISNULL = new Op<Boolean>(Object.class);
	Op<Boolean> ISTYPEOF = new Op<Boolean>(Object.class, Class.class);
	Op<Boolean> NE_PRIMITIVE = new Op<Boolean>(Object_x_2);
	Op<Boolean> NE_OBJECT = new Op<Boolean>(Object_x_2);
	Op<Boolean> NOTIN = new Op<Boolean>(Object_x_2);
	
	// collection
	Op<Boolean> COL_ISEMPTY = new Op<Boolean>(Object.class);
	Op<Boolean> COL_ISNOTEMPTY = new Op<Boolean>(Object.class);

	// Boolean
	Op<Boolean> AND = new Op<Boolean>(Boolean_x_2);
	Op<Boolean> NOT = new Op<Boolean>(Boolean.class);
	Op<Boolean> OR = new Op<Boolean>(Boolean_x_2);
	Op<Boolean> XNOR = new Op<Boolean>(Boolean_x_2);
	Op<Boolean> XOR = new Op<Boolean>(Boolean_x_2);

	// Comparable
	Op<Boolean> BETWEEN = new Op<Boolean>(Comparable_x_3);
	Op<Boolean> GOE = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> GT = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> LOE = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> LT = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> NOTBETWEEN = new Op<Boolean>(Comparable_x_3);
	Op<Number> NUMCAST = new Op<Number>(Number.class, Class.class);
	Op<String> STRING_CAST = new Op<String>(Object.class);

	// Date / Comparable
	Op<Boolean> AFTER = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> BEFORE = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> AOE = new Op<Boolean>(Comparable_x_2);
	Op<Boolean> BOE = new Op<Boolean>(Comparable_x_2);

	// Number
	Op<Number> ADD = new Op<Number>(Number_x_2);
	Op<Number> DIV = new Op<Number>(Number_x_2);
	Op<Number> MOD = new Op<Number>(Number_x_2);
	Op<Number> MULT = new Op<Number>(Number_x_2);
	Op<Number> SUB = new Op<Number>(Number_x_2);

	// String
	Op<Character> CHAR_AT = new Op<Character>(String.class, Integer.class);
	Op<String> CONCAT = new Op<String>(String_x_2);
	Op<Boolean> LIKE = new Op<Boolean>(String_x_2);
	Op<String> LOWER = new Op<String>(String.class);
	Op<String> SUBSTR1ARG = new Op<String>(String.class, Integer.class);
	Op<String> SUBSTR2ARGS = new Op<String>(String.class, Integer.class,Integer.class);
	Op<String> SPLIT = new Op<String>(String_x_2);
	Op<String> TRIM = new Op<String>(String.class);
	Op<String> UPPER = new Op<String>(String.class);
	Op<Boolean> MATCHES = new Op<Boolean>(String_x_2);
	Op<Number> STRING_LENGTH = new Op<Number>(String.class);
	Op<Number> LAST_INDEX_2ARGS = new Op<Number>(String.class, String.class,Integer.class);
	Op<Number> LAST_INDEX = new Op<Number>(String_x_2);
	Op<Boolean> STRING_ISEMPTY = new Op<Boolean>(String.class);
	Op<Boolean> STARTSWITH = new Op<Boolean>(String_x_2);
	Op<Boolean> STARTSWITH_IC = new Op<Boolean>(String_x_2);
	Op<Number> INDEXOF_2ARGS = new Op<Number>(String.class, String.class,Integer.class);
	Op<Number> INDEXOF = new Op<Number>(String.class, String.class);
	Op<Boolean> EQ_IGNORECASE = new Op<Boolean>(String_x_2);
	Op<Boolean> ENDSWITH = new Op<Boolean>(String_x_2);
	Op<Boolean> ENDSWITH_IC = new Op<Boolean>(String_x_2);
	Op<Boolean> CONTAINS = new Op<Boolean>(String_x_2);

	// subquery operations
	Op<Boolean> EXISTS = new Op<Boolean>(Object.class);

	public static final List<Op<?>> equalsOps = Collections.unmodifiableList(Arrays.<Op<?>> asList(EQ_OBJECT, EQ_PRIMITIVE));

	public static final List<Op<?>> notEqualsOps = Collections.unmodifiableList(Arrays.<Op<?>> asList(NE_OBJECT, NE_PRIMITIVE));

	public static final List<Op<?>> compareOps = Collections.unmodifiableList(Arrays.<Op<?>> asList(EQ_OBJECT, EQ_PRIMITIVE,LT, GT, GOE, LOE));

	/**
	 * Aggreate operators
	 * 
	 */
	public interface OpNumberAgg {
		Op<Number> AVG_AGG = new Op<Number>(Number.class);
		Op<Number> MAX_AGG = new Op<Number>(Number.class);
		Op<Number> MIN_AGG = new Op<Number>(Number.class);
	}

	/**
	 * Date and time operators
	 */
	public interface OpDateTime {
		Op<java.util.Date> CURRENT_DATE = new Op<java.util.Date>();
		Op<java.util.Date> CURRENT_TIME = new Op<java.util.Date>();
		Op<java.util.Date> CURRENT_TIMESTAMP = new Op<java.util.Date>();
		Op<Integer> DAY = new Op<Integer>(java.util.Date.class);
		Op<Integer> HOUR = new Op<Integer>(java.util.Date.class);
		Op<Integer> MINUTE = new Op<Integer>(java.util.Date.class);
		Op<Integer> MONTH = new Op<Integer>(java.util.Date.class);
		Op<Integer> SECOND = new Op<Integer>(java.util.Date.class);
		Op<java.util.Date> SYSDATE = new Op<java.util.Date>();
		Op<Integer> YEAR = new Op<Integer>(java.util.Date.class);
		Op<Integer> WEEK = new Op<Integer>(java.util.Date.class);
		Op<Integer> DAY_OF_WEEK = new Op<Integer>(java.util.Date.class);
		Op<Integer> DAY_OF_MONTH = new Op<Integer>(java.util.Date.class);
		Op<Integer> DAY_OF_YEAR = new Op<Integer>(java.util.Date.class);
	}

	/**
	 * Math operators
	 * 
	 */
	public interface OpMath {
		Op<Number> ABS = new Op<Number>(Number.class);
		Op<Number> ACOS = new Op<Number>(Number.class);
		Op<Number> ASIN = new Op<Number>(Number.class);
		Op<Number> ATAN = new Op<Number>(Number.class);
		Op<Number> CEIL = new Op<Number>(Number.class);
		Op<Number> COS = new Op<Number>(Number.class);
		Op<Number> TAN = new Op<Number>(Number.class);
		Op<Number> SQRT = new Op<Number>(Number.class);
		Op<Number> SIN = new Op<Number>(Number.class);
		Op<Number> ROUND = new Op<Number>(Number.class);
		Op<Number> RANDOM = new Op<Number>();
		Op<Number> POWER = new Op<Number>(Number_x_2);
		Op<Number> MIN = new Op<Number>(Number_x_2);
		Op<Number> MAX = new Op<Number>(Number_x_2);
		Op<Number> LOG10 = new Op<Number>(Number.class);
		Op<Number> LOG = new Op<Number>(Number.class);
		Op<Number> FLOOR = new Op<Number>(Number.class);
		Op<Number> EXP = new Op<Number>(Number.class);
	}

	/**
	 * String operators
	 */
	public interface OpString {
		Op<String> LTRIM = new Op<String>(String.class);
		Op<String> RTRIM = new Op<String>(String.class);
		Op<String> SPACE = new Op<String>(Integer.class);
	}
}
