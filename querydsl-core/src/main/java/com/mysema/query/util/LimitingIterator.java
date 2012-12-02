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
package com.mysema.query.util;

import java.util.Iterator;

import com.mysema.query.QueryModifiers;

/**
 * LimitingIterator is and Iterator adapter which takes limit and offset into account
 *
 * @author tiwe
 */
public class LimitingIterator<E> implements Iterator<E> {

    public static <T> Iterator<T> create(Iterator<T> iterator, QueryModifiers modifiers) {
        if (modifiers.isRestricting()) {
            if (modifiers.getOffset() != null) {
                int counter = 0;
                while (iterator.hasNext() && counter < modifiers.getOffset()) {
                    counter++;
                    iterator.next();
                }
            }
            if (modifiers.getLimit() != null) {
                iterator = new LimitingIterator<T>(iterator, modifiers.getLimit());
            }
        }
        return iterator;
    }

    private long counter;

    private final long limit;

    private final Iterator<E> original;

    LimitingIterator(Iterator<E> iterator, long limit) {
        this.original = iterator;
        this.limit = limit;
    }

    @Override
    public boolean hasNext() {
        return original.hasNext() && counter < limit;
    }

    @Override
    public E next() {
        counter++;
        return original.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
