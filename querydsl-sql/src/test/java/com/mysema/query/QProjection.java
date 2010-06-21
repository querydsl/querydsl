/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.ExprConst;

public class QProjection extends EConstructor<Projection>{

    private static final long serialVersionUID = -7330905848558102164L;

    public QProjection(Expr<?>... args) {
    super(Projection.class, new Class[0], args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Projection newInstance(final Object... args){
    return new Projection(){

            @Override
            public <T> T get(int index, Class<T> type) {
            return (T) args[index];
            }

            @Override
            public <T> T get(Expr<T> expr) {
            int index = getArgs().indexOf(expr);
            return index != -1 ? (T) args[index] : null;
            }
            
            @Override
            public <T> Expr<T> getExpr(Expr<T> expr){
            T val = get(expr);
            return val != null ? ExprConst.create(val) : null;
            }
            
            @Override
            public <T> Expr<T> getExpr(int index, Class<T> type){
            T val = (T)args[index];
            return val != null ? ExprConst.create(val) : null;
             }

        @Override
            public Object[] toArray() {
            return args;
            }
        
    };
    }
    
}
