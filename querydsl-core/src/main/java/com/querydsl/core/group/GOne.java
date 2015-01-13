/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.group;

import com.querydsl.core.types.Expression;

/**
 * @author tiwe
 *
 * @param <T>
 */
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