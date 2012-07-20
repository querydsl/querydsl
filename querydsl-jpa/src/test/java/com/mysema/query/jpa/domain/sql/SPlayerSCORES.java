package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPlayerSCORES is a Querydsl query type for SPlayerSCORES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPlayerSCORES extends com.mysema.query.sql.RelationalPathBase<SPlayerSCORES> {

    private static final long serialVersionUID = 1597009331;

    public static final SPlayerSCORES PlayerSCORES = new SPlayerSCORES("Player_SCORES");

    public final NumberPath<Long> playerID = createNumber("Player_ID", Long.class);

    public final NumberPath<Integer> scores = createNumber("SCORES", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SPlayer> playerSCORESPlayerIDFK = createForeignKey(playerID, "id");

    public SPlayerSCORES(String variable) {
        super(SPlayerSCORES.class, forVariable(variable), "null", "Player_SCORES");
    }

    public SPlayerSCORES(Path<? extends SPlayerSCORES> path) {
        super(path.getType(), path.getMetadata(), "null", "Player_SCORES");
    }

    public SPlayerSCORES(PathMetadata<?> metadata) {
        super(SPlayerSCORES.class, metadata, "null", "Player_SCORES");
    }

}

