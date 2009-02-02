/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

/**
 * Custom provides base types for custom expresions with integrated
 * serialization patterns
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Custom<T> {

    Expr<?>[] getArgs();

    java.lang.String getPattern();

    /**
     * Custom boolean expressions
     */
    public static abstract class CBoolean extends Expr.EBoolean implements
            Custom<java.lang.Boolean> {
    }

    /**
     * Custom comparable expression
     */
    public static abstract class CComparable<T extends java.lang.Comparable<T>>
            extends Expr.EComparable<T> implements Custom<T> {
        public CComparable(Class<T> type) {
            super(type);
        }
    }

    public static abstract class CNumber<T extends java.lang.Number & java.lang.Comparable<T>>
            extends Expr.ENumber<T> implements Custom<T> {
        public CNumber(Class<T> type) {
            super(type);
        }
    }

    /**
     * Custom String expression
     */
    public static abstract class CString extends Expr.EString implements
            Custom<java.lang.String> {
    }

}
