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
package com.querydsl.sql;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code SQLBindings} provides the SQL query string and bindings
 *
 * @author tiwe
 *
 */
public class SQLBindings {

    private static final Logger log = LoggerFactory.getLogger(SQLBindings.class);

    private final String sql;

    private final List<Object> bindings;

    @Deprecated
    public SQLBindings(String sql, ImmutableList<Object> bindings) {
        this(sql, (List<Object>) bindings);
        log.warn("Using deprecated SQLBindings constructor");
    }

    public SQLBindings(String sql, List<Object> bindings) {
        this.sql = sql;
        this.bindings = Collections.unmodifiableList(bindings);
    }

    public String getSQL() {
        return sql;
    }

    public List<Object> getNullFriendlyBindings() {
        return bindings;
    }

    /**
     * Returns the bindings for this instance.
     *
     * @deprecated use {@link #getNullFriendlyBindings()} instead - this method is broken as null is a meaningful element
     * and ImmutableList is null-hostile.
     * @return an ImmutableList copy of the contents of {@link #getNullFriendlyBindings()}, with nulls removed
     */
    @Deprecated
    public ImmutableList<Object> getBindings() {
        final ImmutableList.Builder<Object> builder = ImmutableList.builder();
        for (Object o : getNullFriendlyBindings()) {
            if (o != null) {
                builder.add(o);
            }
        }

        return builder.build();
    }

}
