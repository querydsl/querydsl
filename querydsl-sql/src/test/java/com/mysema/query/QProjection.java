/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;

public class QProjection extends ConstructorExpression<Projection>{

    private static final long serialVersionUID = -7330905848558102164L;

    public QProjection(Expression<?>... args) {
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
            public <T> T get(Expression<T> expr) {
                int index = getArgs().indexOf(expr);
                return index != -1 ? (T) args[index] : null;
            }

            @Override
            public <T> Expression<T> getExpr(Expression<T> expr){
                T val = get(expr);
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public <T> Expression<T> getExpr(int index, Class<T> type){
                T val = (T)args[index];
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public Object[] toArray() {
                return args;
            }

        };
    }

}
