package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPlayer extends com.mysema.query.sql.RelationalPathBase<SPlayer> {

    private static final long serialVersionUID = 762379026;

    public static final SPlayer player = new SPlayer("player_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPlayer> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPlayerScores> _fkd5dc571fd8736d5c = createInvForeignKey(id, "Player_id");

    public final com.mysema.query.sql.ForeignKey<SPlayerSCORES2> _playerSCORESPlayerIDFK = createInvForeignKey(id, "Player_ID");

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable), "null", "player_");
    }

    public SPlayer(Path<? extends SPlayer> path) {
        super(path.getType(), path.getMetadata(), "null", "player_");
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata, "null", "player_");
    }

}

