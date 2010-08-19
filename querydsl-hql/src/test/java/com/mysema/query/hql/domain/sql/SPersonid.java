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
 * SPersonid is a Querydsl query type for SPersonid
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PERSONID")
public class SPersonid extends EntityPathBase<SPersonid> {

    public final PString country = createString("COUNTRY");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable));
    }

    public SPersonid(BeanPath<? extends SPersonid> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

