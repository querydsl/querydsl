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
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PNumber;

/**
 * SBar is a Querydsl query type for SBar
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="BAR")
public class SBar extends EntityPathBase<SBar> {

    public final PComparable<java.util.Date> date = createComparable("DATE", java.util.Date.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable));
    }

    public SBar(BeanPath<? extends SBar> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

