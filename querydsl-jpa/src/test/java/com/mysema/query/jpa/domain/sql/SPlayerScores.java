package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SPlayerScores extends com.mysema.query.sql.RelationalPathBase<SPlayerScores> {

    private static final long serialVersionUID = 1627547091;

    public static final SPlayerScores playerScores = new SPlayerScores("PLAYER_SCORES");

    public final NumberPath<Long> playerId = createNumber("PLAYER_ID", Long.class);

    public final NumberPath<Integer> scores = createNumber("SCORES", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SPlayer> fkd5dc571fd8736d5c = createForeignKey(playerId, "ID");

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable), "APP", "PLAYER_SCORES");
    }

    public SPlayerScores(Path<? extends SPlayerScores> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PLAYER_SCORES");
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata, "APP", "PLAYER_SCORES");
    }

}

