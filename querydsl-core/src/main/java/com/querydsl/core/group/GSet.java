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

import java.util.LinkedHashSet;
import java.util.Set;

import com.querydsl.core.types.Expression;

/**
 * @author tiwe
 *
 * @param <T>
 */
class GSet<T> extends AbstractGroupExpression<T, Set<T>> { 
    
    private static final long serialVersionUID = -1575808026237160843L;

    public GSet(Expression<T> expr) {
        super(Set.class, expr);
    }

    @Override
    public GroupCollector<T,Set<T>> createGroupCollector() {
        return new GroupCollector<T,Set<T>>() {

            private final Set<T> set = new LinkedHashSet<T>();
            
            @Override
            public void add(T o) {
                if (o != null) {
                    set.add(o);    
                }                
            }

            @Override
            public Set<T> get() {
                return set;
            }
            
        };
    }
}