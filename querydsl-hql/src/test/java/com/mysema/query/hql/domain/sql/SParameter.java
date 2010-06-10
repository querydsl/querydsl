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
 * SParameter is a Querydsl query type for SParameter
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PARAMETER")
public class SParameter extends PEntity<SParameter> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public SParameter(String variable) {
        super(SParameter.class, forVariable(variable));
    }

    public SParameter(PEntity<? extends SParameter> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SParameter(PathMetadata<?> metadata) {
        super(SParameter.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

