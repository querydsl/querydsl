/*
 * Copyright 2021, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.commons.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface CloseableIterator<T> extends Iterator<T>, AutoCloseable {

    static <T> CloseableIterator<T> fromIterator(@NotNull final Iterator<T> iterator) {
        return combine(iterator, iterator instanceof AutoCloseable ? (AutoCloseable) iterator : null);
    }

    static <T> CloseableIterator<T> combine(@NotNull final Iterator<T> iterator, @Nullable final AutoCloseable closeable) {
        return new CloseableIterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }

            @Override
            public void close() {
                if (closeable != null) {
                    try {
                        (closeable).close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    @Override
    void close();

    /**
     * returns a list of all remaining items and closes the iterator.
     */
    @NotNull
    default List<T> asList() {
        List<T> list = new ArrayList<>();
        try {
            while (hasNext()) {
                list.add(next());
            }
        } finally {
            close();
        }
        return list;
    }
}
