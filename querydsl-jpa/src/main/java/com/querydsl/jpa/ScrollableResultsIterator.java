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
package com.querydsl.jpa;

import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import org.hibernate.ScrollableResults;

import com.mysema.commons.lang.CloseableIterator;

/**
 * ScrollableResultsIterator is an {@link CloseableIterator} adapter for ScrollableResults
 *
 * @author tiwe
 *
 * @param <T>
 */
public class ScrollableResultsIterator<T> implements CloseableIterator<T> {

    private final ScrollableResults results;

    private final boolean asArray;

    @Nullable
    private Boolean hasNext;

    public ScrollableResultsIterator(ScrollableResults results) {
        this(results, false);
    }

    public ScrollableResultsIterator(ScrollableResults results, boolean asArray) {
        this.results = results;
        this.asArray = asArray;
    }

    @Override
    public void close() {
        results.close();
    }

    @Override
    public boolean hasNext() {
        if (hasNext == null) {
            hasNext = results.next();
        }
        return hasNext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        if (hasNext()) {
            hasNext = null;
            if (asArray) {
                return (T) results.get();
            } else {
                return (T) results.get(0);
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
