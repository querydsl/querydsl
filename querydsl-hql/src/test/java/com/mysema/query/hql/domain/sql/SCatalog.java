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
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PNumber;

/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="CATALOG")
public class SCatalog extends EntityPathBase<SCatalog> {

    public final PComparable<java.util.Date> effectivedate = createComparable("EFFECTIVEDATE", java.util.Date.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable));
    }

    public SCatalog(BeanPath<? extends SCatalog> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

