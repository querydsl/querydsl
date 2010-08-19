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
 * SCatalogPrice is a Querydsl query type for SCatalogPrice
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CATALOG_PRICE")
public class SCatalogPrice extends EntityPathBase<SCatalogPrice> {

    public final PNumber<Integer> catalogId = createNumber("CATALOG_ID", Integer.class);

    public final PNumber<Long> pricesId = createNumber("PRICES_ID", Long.class);

    public SCatalogPrice(String variable) {
        super(SCatalogPrice.class, forVariable(variable));
    }

    public SCatalogPrice(BeanPath<? extends SCatalogPrice> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCatalogPrice(PathMetadata<?> metadata) {
        super(SCatalogPrice.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

