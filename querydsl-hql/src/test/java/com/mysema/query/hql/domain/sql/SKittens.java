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
 * SKittens is a Querydsl query type for SKittens
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS")
public class SKittens extends PEntity<SKittens> {

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> ind = createNumber("IND", Integer.class);

    public final PNumber<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable));
    }

    public SKittens(PEntity<? extends SKittens> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

