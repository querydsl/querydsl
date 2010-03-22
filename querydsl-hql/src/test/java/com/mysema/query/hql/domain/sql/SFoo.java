/*
 * Copyright (c) 2009 Mysema Ltd.
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
 * SFoo is a Querydsl query type for SFoo
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="FOO")
public class SFoo extends PEntity<SFoo> {

    public final PString bar = createString("BAR");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PComparable<java.util.Date> startdate = createComparable("STARTDATE", java.util.Date.class);

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable));
    }

    public SFoo(PEntity<? extends SFoo> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

