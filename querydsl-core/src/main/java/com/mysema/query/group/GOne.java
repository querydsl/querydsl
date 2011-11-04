/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

class GOne<T> extends AbstractGroupExpression<T, T> {

    private static final long serialVersionUID = 3518868612387641383L;

    @SuppressWarnings("unchecked")
    public GOne(Expression<T> expr) {
        super((Class)expr.getType(), expr);
    }

    @Override
    public GroupCollector<T,T> createGroupCollector() {
        return new GroupCollector<T,T>() {
            private boolean first = true;
            
            private T val;
            
            @Override
            public void add(T o) {
                if (first) {
                    val = o;
                    first = false;
                }
            }
    
            @Override
            public T get() {
                return val;
            }
        };
    }
}