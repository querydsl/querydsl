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
package com.querydsl.core;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Objects;

/**
 * {@code QueryModifiers} combines limit and offset info into a single immutable value type.
 *
 * @author tiwe
 */
public final class QueryModifiers implements Serializable {

    private static final long serialVersionUID = 2934344588433680339L;

    /**
     * No limit and not offset restriction
     */
    public static final QueryModifiers EMPTY = new QueryModifiers();

    private static int toInt(Long l) {
        if (l <= Integer.MAX_VALUE) {
            return l.intValue();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static QueryModifiers limit(@Range(from = 0, to = Integer.MAX_VALUE) long limit) {
        return new QueryModifiers(limit, null);
    }

    public static QueryModifiers offset(@Range(from = 0, to = Integer.MAX_VALUE) long offset) {
        return new QueryModifiers(null, offset);
    }

    @Nullable
    private final Long limit, offset;

    private QueryModifiers() {
        limit = null;
        offset = null;
    }

    public QueryModifiers(@Nullable Long limit, @Nullable Long offset) {
        this.limit = limit;
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        this.offset = offset;
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative.");
        }
    }

    public QueryModifiers(QueryModifiers modifiers) {
        this.limit = modifiers.getLimit();
        this.offset = modifiers.getOffset();
    }

    @Nullable
    public Long getLimit() {
        return limit;
    }

    @Nullable
    public Integer getLimitAsInteger() {
        return limit != null ? toInt(limit) : null;
    }

    @Nullable
    public Long getOffset() {
        return offset;
    }

    @Nullable
    public Integer getOffsetAsInteger() {
        return offset != null ? toInt(offset) : null;
    }

    /**
     * Checks if is restricting.
     *
     * @return true, if is restricting
     */
    public boolean isRestricting() {
        return limit != null || offset != null;
    }

    /**
     * Get a sublist based on the restriction of limit and offset
     *
     * @param <T>
     * @param list list to be handled
     * @return sublist with limit and offset applied
     */
    public <T> List<T> subList(List<T> list) {
        if (!list.isEmpty()) {
            int from = offset != null ? toInt(offset) : 0;
            int to = limit != null ? (from + toInt(limit)) : list.size();
            return list.subList(from, Math.min(to,list.size()));
        } else {
            return list;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof QueryModifiers) {
            QueryModifiers qm = (QueryModifiers) o;
            return Objects.equal(qm.getLimit(), limit) && Objects.equal(qm.getOffset(), offset);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(limit, offset);
    }

}
