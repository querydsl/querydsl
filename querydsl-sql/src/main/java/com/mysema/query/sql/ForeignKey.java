/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import net.jcip.annotations.Immutable;

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
 * @param <P>
 */
@Immutable
public class ForeignKey <E>{

    private final RelationalPath<?> entity;

    private final List<? extends Path<?>> localColumns;

    private final List<String> foreignColumns;

    public ForeignKey(RelationalPath<?> entity, Path<?> localColumn, String foreignColumn) {
        this(entity, Arrays.<Path<?>>asList(localColumn), Arrays.asList(foreignColumn));
    }

    public ForeignKey(RelationalPath<?> entity, List<? extends Path<?>> localColumns, List<String> foreignColumns) {
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
