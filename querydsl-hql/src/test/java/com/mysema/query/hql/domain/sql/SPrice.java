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
 * SPrice is a Querydsl query type for SPrice
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PRICE")
public class SPrice extends EntityPathBase<SPrice> {

    public final PNumber<Long> amount = createNumber("AMOUNT", Long.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public SPrice(String variable) {
        super(SPrice.class, forVariable(variable));
    }

    public SPrice(BeanPath<? extends SPrice> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPrice(PathMetadata<?> metadata) {
        super(SPrice.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

