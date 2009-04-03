/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.util.List;

/**
 * Custom provides base types for custom expresions with integrated
 * serialization patterns
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Custom<T> {

    List<Expr<?>> getArgs();
    
    Expr<?> getArg(int index);

    java.lang.String getPattern();
    
    public static abstract class CBoolean extends Expr.EBoolean implements
            Custom<java.lang.Boolean> {
        public Expr<?> getArg(int index) {return getArgs().get(index);}        
    }

    public static abstract class CComparable<T extends java.lang.Comparable<? super T>>
            extends Expr.EComparable<T> implements Custom<T> {
        public CComparable(Class<T> type) {
            super(type);
        }
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

    public static abstract class CNumber<T extends java.lang.Number & java.lang.Comparable<? super T>>
            extends Expr.ENumber<T> implements Custom<T> {
        public CNumber(Class<T> type) {
            super(type);
        }
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

    public static abstract class CSimple<T> extends Expr.ESimple<T> implements Custom<T>{
        public CSimple(Class<? extends T> type) {
            super(type);
        }        
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

    public static abstract class CString extends Expr.EString implements
            Custom<java.lang.String> {
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

}
