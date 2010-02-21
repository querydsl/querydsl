package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PLAYER_SCORES")
public class SPlayerScores extends PEntity<SPlayerScores> {

    public final PNumber<Integer> element = createNumber("ELEMENT", Integer.class);

    public final PNumber<Long> playerId = createNumber("PLAYER_ID", Long.class);

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable));
    }

    public SPlayerScores(PEntity<? extends SPlayerScores> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata);
    }

}

