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
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NAMELIST_NAMES")
public class SNamelistNames extends PEntity<SNamelistNames> {

    public final PString element = createString("ELEMENT");

    public final PNumber<Long> namelistId = createNumber("NAMELIST_ID", Long.class);

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable));
    }

    public SNamelistNames(PEntity<? extends SNamelistNames> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

