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
package com.querydsl.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.Nullable;

import org.hibernate.ScrollableResults;

import com.mysema.commons.lang.CloseableIterator;

/**
 * {@code ScrollableResultsIterator} is a {@link CloseableIterator} adapter for ScrollableResults
 *
 * <p>Hibernate 5 exposes {@code Object[] get()} and {@code Object get(int)}, while Hibernate 6 only
 * exposes a generic {@code R get()}. Those methods have different JVM descriptors, so this adapter
 * invokes {@code get()} reflectively and normalizes single-value vs array rows for both versions.
 *
 * @author tiwe
 *
 * @param <T>
 */
public class ScrollableResultsIterator<T> implements CloseableIterator<T> {

    private final ScrollableResults results;

    private final boolean asArray;

    private final Method getMethod;

    @Nullable
    private Boolean hasNext;

    public ScrollableResultsIterator(ScrollableResults results) {
        this(results, false);
    }

    public ScrollableResultsIterator(ScrollableResults results, boolean asArray) {
        this.results = results;
        this.asArray = asArray;
        try {
            // Resolve on the concrete runtime type so Hibernate 5 (Object[] get) and
            // Hibernate 6 (R get / Object get) both work when Querydsl is compiled against 5.x.
            this.getMethod = results.getClass().getMethod("get");
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("ScrollableResults without get()", e);
        }
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
            return (T) extractValue(currentRow(), asArray);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private Object currentRow() {
        try {
            return getMethod.invoke(results);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to read ScrollableResults row", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new IllegalStateException("Failed to read ScrollableResults row", cause);
        }
    }

    /**
     * Normalize a Hibernate 5 {@code Object[]} row or a Hibernate 6 typed row value.
     */
    static Object extractValue(Object row, boolean asArray) {
        if (asArray) {
            if (row instanceof Object[]) {
                return row;
            }
            return new Object[]{row};
        }
        if (row instanceof Object[]) {
            Object[] cells = (Object[]) row;
            return cells.length == 0 ? null : cells[0];
        }
        return row;
    }

}
