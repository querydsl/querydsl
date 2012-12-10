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
package com.mysema.query.sql;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.SimplePath;

/**
 * ForeignKey defines a foreign key on a table to another table
 *
 * @author tiwe
 *
 * @param <E>
 */
public class ForeignKey <E> implements Serializable {

    private static final long serialVersionUID = 2260578033772289023L;

    private final RelationalPath<?> entity;

    private final List<? extends Path<?>> localColumns;

    private final List<String> foreignColumns;

    public ForeignKey(RelationalPath<?> entity, Path<?> localColumn, String foreignColumn) {
        this(entity, ImmutableList.of(localColumn), ImmutableList.of(foreignColumn));
    }

    public ForeignKey(RelationalPath<?> entity, List<? extends Path<?>> localColumns, 
            List<String> foreignColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
        this.foreignColumns = foreignColumns;
    }

    public RelationalPath<?> getEntity() {
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }

    public List<String> getForeignColumns() {
        return foreignColumns;
    }

    @SuppressWarnings("unchecked")
    public Predicate on(RelationalPath<E> entity) {
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < localColumns.size(); i++) {
            Expression<Object> local = (Expression<Object>)localColumns.get(i);
            Expression<?> foreign = new SimplePath(local.getType(), entity, foreignColumns.get(i));
            builder.and(ExpressionUtils.eq(local,foreign));
        }
        return builder.getValue();
    }

}
