/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

public class Constructor<D> extends Expr<D> {
    private final Expr<?>[] args;
    public Constructor(Class<D> type, Expr<?>... args) {
        super(type);
        this.args = args;
    }
    public Expr<?>[] getArgs() {
        return args;
    }
    
    public static class CArray<D> extends Constructor<D[]> {
        private Class<D> elementType;
        public CArray(Class<D> type, Expr<D>... args) {
            super(null, args);
            this.elementType = type;
        }
        public Class<D> getElementType(){
            return elementType;
        }
    }
}