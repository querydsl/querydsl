/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PNumber;

/**
 * SKittensArray is a Querydsl query type for SKittensArray
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS_ARRAY")
public class SKittensArray extends EntityPathBase<SKittensArray> implements RelationalPath<SKittensArray>{

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> arrayindex = createNumber("ARRAYINDEX", Integer.class);

    public final PNumber<Integer> kittensarrayId = createNumber("KITTENSARRAY_ID", Integer.class);

    public SKittensArray(String variable) {
        super(SKittensArray.class, forVariable(variable));
    }

    public SKittensArray(BeanPath<? extends SKittensArray> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittensArray(PathMetadata<?> metadata) {
        super(SKittensArray.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(animalId, arrayindex, kittensarrayId);
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public PrimaryKey<SKittensArray> getPrimaryKey() {
        return null;
    }

}

