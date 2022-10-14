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
package com.querydsl.mongodb;

import com.querydsl.core.types.Operator;

/**
 * MongoDB specific operators
 *
 * @author tiwe
 * @author sangyong choi
 */
public enum MongodbOps implements Operator {
    NEAR(Boolean.class),
    GEO_WITHIN_BOX(Boolean.class),
    ELEM_MATCH(Boolean.class),
    NO_MATCH(Boolean.class),
    NEAR_SPHERE(Boolean.class),
    GEO_INTERSECTS(Boolean.class),
    ALL(Boolean.class);

    private final Class<?> type;

    MongodbOps(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
