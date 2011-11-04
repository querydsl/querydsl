/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.Expression;

class GList<T> extends AbstractGroupExpression<T, List<T>> {
    
    private static final long serialVersionUID = -5613861506383727078L;

    public GList(Expression<T> expr) {
        super(List.class, expr);
    }

    @Override
    public GroupCollector<T, List<T>> createGroupCollector() {
        return new GroupCollector<T, List<T>>() {

            private final List<T> list = new ArrayList<T>();
            
            @Override
            public void add(T o) {
                list.add(o);
            }

            @Override
            public List<T> get() {
                return list;
            }
            
        };
    }

}