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
 * SAccount is a Querydsl query type for SAccount
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ACCOUNT")
public class SAccount extends PEntity<SAccount> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final PString somedata = createString("SOMEDATA");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable));
    }

    public SAccount(PEntity<? extends SAccount> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

