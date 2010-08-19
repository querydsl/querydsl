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
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PLAYER_SCORES")
public class SPlayerScores extends EntityPathBase<SPlayerScores> {

    public final PNumber<Integer> element = createNumber("ELEMENT", Integer.class);

    public final PNumber<Long> playerId = createNumber("PLAYER_ID", Long.class);

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable));
    }

    public SPlayerScores(BeanPath<? extends SPlayerScores> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

