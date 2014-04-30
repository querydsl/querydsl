package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPlayer extends com.mysema.query.sql.RelationalPathBase<SPlayer> {

    private static final long serialVersionUID = -2136053875;

    public static final SPlayer player_ = new SPlayer("player_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPlayer> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPlayerScores> _fkd5dc571fd8736d5c = createInvForeignKey(id, "Player_id");

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable), "null", "player_");
        addMetadata();
    }

    public SPlayer(String variable, String schema, String table) {
        super(SPlayer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPlayer(Path<? extends SPlayer> path) {
        super(path.getType(), path.getMetadata(), "null", "player_");
        addMetadata();
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata, "null", "player_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

