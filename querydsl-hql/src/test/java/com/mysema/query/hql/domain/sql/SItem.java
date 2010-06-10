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
import com.mysema.query.types.path.PString;

/**
 * SItem is a Querydsl query type for SItem
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ITEM")
public class SItem extends PEntity<SItem> {

    public final PNumber<Long> currentstatusId = createNumber("CURRENTSTATUS_ID", Long.class);

    public final PString dtype = createString("DTYPE");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> name = createNumber("NAME", Integer.class);

    public final PNumber<Long> productId = createNumber("PRODUCT_ID", Long.class);

    public final PNumber<Long> statusId = createNumber("STATUS_ID", Long.class);

    public SItem(String variable) {
        super(SItem.class, forVariable(variable));
    }

    public SItem(PEntity<? extends SItem> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SItem(PathMetadata<?> metadata) {
        super(SItem.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

