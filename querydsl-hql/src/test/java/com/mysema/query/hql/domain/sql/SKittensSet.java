/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;

/**
 * SKittensSet is a Querydsl query type for SKittensSet
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS_SET")
public class SKittensSet extends PEntity<SKittensSet> {

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> kittenssetId = createNumber("KITTENSSET_ID", Integer.class);

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable));
    }

    public SKittensSet(PEntity<? extends SKittensSet> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

