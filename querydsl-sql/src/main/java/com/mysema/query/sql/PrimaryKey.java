/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Path;

/**
 * PrimaryKey defines a primary key on table
 *
 * @author tiwe
 */
@Immutable
public class PrimaryKey <E> {

    private final RelationalPath<?> entity;

    private final List<? extends Path<?>> localColumns;

    public PrimaryKey(RelationalPath<?> entity, Path<?>... localColumns) {
        this(entity, Arrays.asList(localColumns));
    }

    public PrimaryKey(RelationalPath<?> entity, List<? extends Path<?>> localColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
    }

    public RelationalPath<?> getEntity() {
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }

}
