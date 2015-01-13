package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SPlayerScores is a Querydsl querydsl type for SPlayerScores
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SPlayerScores extends com.querydsl.sql.RelationalPathBase<SPlayerScores> {

    private static final long serialVersionUID = 1627547091;

    public static final SPlayerScores PlayerScores = new SPlayerScores("Player_scores");

    public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

    public final NumberPath<Integer> scores = createNumber("scores", Integer.class);

    public final com.querydsl.sql.ForeignKey<SPlayer> fkd5dc571fd8736d5c = createForeignKey(playerId, "id");

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable), "", "Player_scores");
        addMetadata();
    }

    public SPlayerScores(String variable, String schema, String table) {
        super(SPlayerScores.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPlayerScores(Path<? extends SPlayerScores> path) {
        super(path.getType(), path.getMetadata(), "", "Player_scores");
        addMetadata();
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata, "", "Player_scores");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(playerId, ColumnMetadata.named("Player_id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(scores, ColumnMetadata.named("scores").withIndex(2).ofType(4).withSize(10));
    }

}

