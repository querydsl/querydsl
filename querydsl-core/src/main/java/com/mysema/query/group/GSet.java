/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.query.group;

import java.util.*;

import com.mysema.query.types.Expression;

/**
 * @author tiwe
 *
 * @param <T>
 */
abstract class GSet<T, S extends Set<T>> extends AbstractGroupExpression<T, S> {

    private static final long serialVersionUID = -1575808026237160843L;

    public GSet(Expression<T> expr) {
        super(Set.class, expr);
    }

    protected abstract S createSet();

    @Override
    public GroupCollector<T, S> createGroupCollector() {
        return new GroupCollector<T, S>() {

            private final S set = createSet();

            @Override
            public void add(T o) {
                if (o != null) {
                    set.add(o);
                }
            }

            @Override
            public S get() {
                return set;
            }

        };
    }

    public static <U> GSet<U, Set<U>> createLinked(Expression<U> expr) {
        return new GSet<U, Set<U>>(expr) {
            @Override
            protected Set<U> createSet() {
                return new LinkedHashSet<U>();
            }
        };
    }

    public static <U extends Comparable<? super U>> GSet<U, SortedSet<U>> createSorted(Expression<U> expr) {
        return new GSet<U, SortedSet<U>>(expr) {
            @Override
            protected SortedSet<U> createSet() {
                return new TreeSet<U>();
            }
        };
    }

    public static <U> GSet<U, SortedSet<U>> createSorted(Expression<U> expr, final Comparator<? super U> comparator) {
        return new GSet<U, SortedSet<U>>(expr) {
            @Override
            protected SortedSet<U> createSet() {
                return new TreeSet<U>(comparator);
            }
        };
    }

}
