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
 * SStore is a Querydsl query type for SStore
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STORE")
public class SStore extends EntityPathBase<SStore> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public SStore(String variable) {
        super(SStore.class, forVariable(variable));
    }

    public SStore(BeanPath<? extends SStore> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

