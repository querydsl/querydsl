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
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;

/**
 * SPerson is a Querydsl query type for SPerson
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PERSON")
public class SPerson extends PEntity<SPerson> {

    public final PComparable<java.util.Date> birthday = createComparable("BIRTHDAY", java.util.Date.class);

    public final PNumber<Long> i = createNumber("I", Long.class);

    public final PString name = createString("NAME");

    public final PNumber<Long> nationalityId = createNumber("NATIONALITY_ID", Long.class);

    public final PNumber<Long> pidId = createNumber("PID_ID", Long.class);

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable));
    }

    public SPerson(PEntity<? extends SPerson> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

