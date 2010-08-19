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
import com.mysema.query.types.path.PString;

/**
 * SLocation is a Querydsl query type for SLocation
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="LOCATION")
public class SLocation extends EntityPathBase<SLocation> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable));
    }

    public SLocation(BeanPath<? extends SLocation> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

