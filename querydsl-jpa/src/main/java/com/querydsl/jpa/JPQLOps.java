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

import com.querydsl.core.types.Operator;

/**
 * {@code JPQLOps} provides JPQL specific operators
 *
 * @author tiwe
 *
 */
public enum JPQLOps implements Operator {
    TREAT(Object.class),
    INDEX(Integer.class),
    TYPE(String.class),
    CAST(Object.class),
    MEMBER_OF(Boolean.class),
    NOT_MEMBER_OF(Boolean.class),
    KEY(Object.class),
    VALUE(Object.class);

    private final Class<?> type;

    private JPQLOps(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
