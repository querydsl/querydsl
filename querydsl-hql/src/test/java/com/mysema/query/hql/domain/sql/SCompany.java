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
 * SCompany is a Querydsl query type for SCompany
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="COMPANY")
public class SCompany extends EntityPathBase<SCompany> {

    public final PNumber<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable));
    }

    public SCompany(BeanPath<? extends SCompany> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

