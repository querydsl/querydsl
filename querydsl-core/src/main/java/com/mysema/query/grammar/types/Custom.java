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

    String getPattern();
    
    public static abstract class CBoolean extends Expr.EBoolean implements
            Custom<Boolean> {
        public Expr<?> getArg(int index) {return getArgs().get(index);}        
    }

    public static abstract class CComparable<T extends Comparable<?>>
            extends Expr.EComparable<T> implements Custom<T> {
        public CComparable(Class<T> type) {
            super(type);
        }
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

    public static abstract class CNumber<T extends Number & Comparable<?>>
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
            Custom<String> {
        public Expr<?> getArg(int index) {return getArgs().get(index);}
    }

}
