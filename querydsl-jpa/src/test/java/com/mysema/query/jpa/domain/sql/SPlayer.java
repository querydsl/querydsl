package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SPlayer extends com.mysema.query.sql.RelationalPathBase<SPlayer> {

    private static final long serialVersionUID = 762379026;

    public static final SPlayer player = new SPlayer("PLAYER_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SPlayer> sql120219232328630 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPlayerScores> _fkd5dc571fd8736d5c = createInvForeignKey(id, "PLAYER_ID");

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable), "APP", "PLAYER_");
    }

    public SPlayer(Path<? extends SPlayer> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "PLAYER_");
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata, "APP", "PLAYER_");
    }

}

