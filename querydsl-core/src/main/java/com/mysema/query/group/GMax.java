/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

class GMax<T extends Comparable<T>> extends AbstractGroupDefinition<T, T> {

    public GMax(Expression<T> expr) {
        super(expr);
    }

    @Override
    public GroupCollector<T,T> createGroupCollector() {
        return new GroupCollector<T,T>() {
            private T max;
            
            @Override
            public void add(T o) {
                if (max != null) {
                    max = o.compareTo(max) > 0 ? o : max;
                } else {
                    max = o;
                }
            }
            @Override
            public T get() {
                return max;
            }                
        };
    }        
}