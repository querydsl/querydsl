/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

/**
 * Custom provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Custom<T> {

    Expr<?>[] getArgs();
    java.lang.String getPattern();
    
//    public interface CustomExpression<T>{
//        Expr<?>[] getArgs();
//        java.lang.String getPattern();
//    } 
   
    public static abstract class Boolean extends Expr.Boolean implements Custom<java.lang.Boolean>{ }
    
    public static abstract class Comparable<T extends java.lang.Comparable<T>> 
        extends Expr.Comparable<T> implements Custom<T>{
        public Comparable(Class<T> type) {
            super(type);
        }
    }    
   
    public static abstract class String extends Expr.String implements Custom<java.lang.String>{ }
    
}
