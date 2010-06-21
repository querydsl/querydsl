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
 * SDepartment is a Querydsl query type for SDepartment
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="DEPARTMENT")
public class SDepartment extends PEntity<SDepartment> {

    public final PNumber<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable));
    }

    public SDepartment(PEntity<? extends SDepartment> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

