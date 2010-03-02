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
 * SCompany is a Querydsl query type for SCompany
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="COMPANY")
public class SCompany extends PEntity<SCompany> {

    public final PNumber<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable));
    }

    public SCompany(PEntity<? extends SCompany> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

