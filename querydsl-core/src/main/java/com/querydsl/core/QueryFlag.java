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
package com.querydsl.core;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.TemplateExpressionImpl;

/**
 * Defines a positioned flag in a Query for customization of querydsl serialization
 *
 * @author tiwe
 *
 */
public class QueryFlag implements Serializable{

    private static final long serialVersionUID = -7131081607441961628L;

    public enum Position {

        /**
         *
         */
        WITH,

        /**
         * Start of the querydsl
         */
        START,

        /**
         * Override for the first element (e.g SELECT, INSERT)
         */
        START_OVERRIDE,

        /**
         * After the first element (after select)
         */
        AFTER_SELECT,

        /**
         * After the projection (after select ...)
         */
        AFTER_PROJECTION,

        /**
         * Before the filter conditions (where)
         */
        BEFORE_FILTERS,

        /**
         * After the filter conditions (where)
         */
        AFTER_FILTERS,

        /**
         * Before group by
         */
        BEFORE_GROUP_BY,

        /**
         * After group by
         */
        AFTER_GROUP_BY,

        /**
         * Before having
         */
        BEFORE_HAVING,

        /**
         * After having
         */
        AFTER_HAVING,

        /**
         * Before order (by)
         */
        BEFORE_ORDER,

        /**
         * After order (by)
         */
        AFTER_ORDER,

        /**
         * After all other tokens
         */
        END

    }

    private final Position position;

    private final Expression<?> flag;

    public QueryFlag(Position position, String flag) {
        this(position, TemplateExpressionImpl.create(Object.class, flag));
    }

    public QueryFlag(Position position, Expression<?> flag) {
        this.position = position;
        this.flag = flag;
    }

    public Position getPosition() {
        return position;
    }

    public Expression<?> getFlag() {
        return flag;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position, flag);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof QueryFlag) {
            QueryFlag other = (QueryFlag)obj;
            return other.position.equals(position) && other.flag.equals(flag);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return position + " : " + flag;
    }
}
