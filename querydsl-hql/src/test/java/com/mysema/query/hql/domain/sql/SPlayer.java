package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PLAYER")
public class SPlayer extends PEntity<SPlayer> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable));
    }

    public SPlayer(PEntity<? extends SPlayer> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

