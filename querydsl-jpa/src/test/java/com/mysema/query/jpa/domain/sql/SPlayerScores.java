package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPlayerScores extends com.mysema.query.sql.RelationalPathBase<SPlayerScores> {

    private static final long serialVersionUID = 1627547091;

    public static final SPlayerScores PlayerScores = new SPlayerScores("Player_scores");

    public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

    public final NumberPath<Integer> scores = createNumber("scores", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SPlayer> fkd5dc571fd8736d5c = createForeignKey(playerId, "id");

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable), "null", "Player_scores");
        addMetadata();
    }

    public SPlayerScores(String variable, String schema, String table) {
        super(SPlayerScores.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPlayerScores(Path<? extends SPlayerScores> path) {
        super(path.getType(), path.getMetadata(), "null", "Player_scores");
        addMetadata();
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata, "null", "Player_scores");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(playerId, ColumnMetadata.named("Player_id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(scores, ColumnMetadata.named("scores").withIndex(2).ofType(4).withSize(10));
    }

}

