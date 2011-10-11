/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

class GOne<T> extends AbstractGroupDefinition<T, T>{

    public GOne(Expression<T> expr) {
        super(expr);
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