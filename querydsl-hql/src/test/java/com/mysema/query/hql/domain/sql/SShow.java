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
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PNumber;

/**
 * SShow is a Querydsl query type for SShow
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SHOW")
public class SShow extends EntityPathBase<SShow> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SShow(String variable) {
        super(SShow.class, forVariable(variable));
    }

    public SShow(BeanPath<? extends SShow> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

