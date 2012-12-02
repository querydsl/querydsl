package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SPlayerSCORES is a Querydsl query type for SPlayerSCORES
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPlayerSCORES2 extends com.mysema.query.sql.RelationalPathBase<SPlayerSCORES2> {

    private static final long serialVersionUID = 1597009331;

    public static final SPlayerSCORES2 PlayerSCORES = new SPlayerSCORES2("Player_SCORES");

    public final NumberPath<Long> playerID = createNumber("Player_ID", Long.class);

    public final NumberPath<Integer> scores = createNumber("SCORES", Integer.class);

    public final com.mysema.query.sql.ForeignKey<SPlayer> playerSCORESPlayerIDFK = createForeignKey(playerID, "id");

    public SPlayerSCORES2(String variable) {
        super(SPlayerSCORES2.class, forVariable(variable), "null", "Player_SCORES");
    }

    public SPlayerSCORES2(Path<? extends SPlayerSCORES2> path) {
        super(path.getType(), path.getMetadata(), "null", "Player_SCORES");
    }

    public SPlayerSCORES2(PathMetadata<?> metadata) {
        super(SPlayerSCORES2.class, metadata, "null", "Player_SCORES");
    }

}

