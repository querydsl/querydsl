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
 * SStoreCustomer is a Querydsl query type for SStoreCustomer
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STORE_CUSTOMER")
public class SStoreCustomer extends EntityPathBase<SStoreCustomer> {

    public final PNumber<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final PNumber<Long> storeId = createNumber("STORE_ID", Long.class);

    public SStoreCustomer(String variable) {
        super(SStoreCustomer.class, forVariable(variable));
    }

    public SStoreCustomer(BeanPath<? extends SStoreCustomer> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStoreCustomer(PathMetadata<?> metadata) {
        super(SStoreCustomer.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

