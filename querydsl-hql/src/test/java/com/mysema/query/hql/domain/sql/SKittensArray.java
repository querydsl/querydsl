/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * SKittensArray is a Querydsl query type for SKittensArray
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS_ARRAY")
public class SKittensArray extends PEntity<SKittensArray> {

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> arrayindex = createNumber("ARRAYINDEX", Integer.class);

    public final PNumber<Integer> kittensarrayId = createNumber("KITTENSARRAY_ID", Integer.class);

    public SKittensArray(String variable) {
        super(SKittensArray.class, forVariable(variable));
    }

    public SKittensArray(PEntity<? extends SKittensArray> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittensArray(PathMetadata<?> metadata) {
        super(SKittensArray.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

