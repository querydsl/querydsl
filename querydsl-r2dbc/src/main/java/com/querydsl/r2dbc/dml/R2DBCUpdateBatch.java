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
package com.querydsl.r2dbc.dml;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;

import java.util.Map;

/**
 * {@code SQLUpdateBatch} defines the state of an SQL UPDATE batch item
 *
 * @author tiwe
 */
public class R2DBCUpdateBatch {

    private final QueryMetadata metadata;

    private final Map<Path<?>, Expression<?>> updates;

    public R2DBCUpdateBatch(QueryMetadata metadata, Map<Path<?>, Expression<?>> updates) {
        this.metadata = metadata;
        this.updates = updates;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }

    public Map<Path<?>, Expression<?>> getUpdates() {
        return updates;
    }

}
