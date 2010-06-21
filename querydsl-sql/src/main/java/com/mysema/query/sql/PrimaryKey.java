/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PEntity;

/**
 * PrimaryKey defines a primary key on table
 *
 * @author tiwe
 *
 * @param <E>
 * @param <P>
 */
public class PrimaryKey <E>{

    private final PEntity<?> entity;

    private final List<? extends Path<?>> localColumns;

    public PrimaryKey(PEntity<?> entity, Path<?>... localColumns) {
        this(entity, Arrays.asList(localColumns));
    }

    public PrimaryKey(PEntity<?> entity, List<? extends Path<?>> localColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
    }

    public PEntity<?> getEntity(){
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }

}
