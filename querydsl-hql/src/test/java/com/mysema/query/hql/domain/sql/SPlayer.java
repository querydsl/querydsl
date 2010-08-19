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

/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PLAYER")
public class SPlayer extends EntityPathBase<SPlayer> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable));
    }

    public SPlayer(BeanPath<? extends SPlayer> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

