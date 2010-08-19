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
 * SShowActs is a Querydsl query type for SShowActs
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SHOW_ACTS")
public class SShowActs extends EntityPathBase<SShowActs> {

    public final PString element = createString("ELEMENT");

    public final PString mapkey = createString("MAPKEY");

    public final PNumber<Integer> showId = createNumber("SHOW_ID", Integer.class);

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable));
    }

    public SShowActs(BeanPath<? extends SShowActs> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

