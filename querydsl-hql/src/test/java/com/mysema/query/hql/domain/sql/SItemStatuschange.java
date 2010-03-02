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
 * SItemStatuschange is a Querydsl query type for SItemStatuschange
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ITEM_STATUSCHANGE")
public class SItemStatuschange extends PEntity<SItemStatuschange> {

    public final PNumber<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final PNumber<Long> statuschangesId = createNumber("STATUSCHANGES_ID", Long.class);

    public SItemStatuschange(String variable) {
        super(SItemStatuschange.class, forVariable(variable));
    }

    public SItemStatuschange(PEntity<? extends SItemStatuschange> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SItemStatuschange(PathMetadata<?> metadata) {
        super(SItemStatuschange.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

