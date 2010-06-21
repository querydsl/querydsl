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
 * SCustomer is a Querydsl query type for SCustomer
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CUSTOMER")
public class SCustomer extends PEntity<SCustomer> {

    public final PNumber<Long> currentorderId = createNumber("CURRENTORDER_ID", Long.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> nameId = createNumber("NAME_ID", Long.class);

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable));
    }

    public SCustomer(PEntity<? extends SCustomer> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

