package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SPlayer is a Querydsl querydsl type for SPlayer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SPlayer extends com.querydsl.sql.RelationalPathBase<SPlayer> {

    private static final long serialVersionUID = -2136053875;

    public static final SPlayer player_ = new SPlayer("player_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SPlayer> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SPlayerScores> _fkd5dc571fd8736d5c = createInvForeignKey(id, "Player_id");

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable), "", "player_");
        addMetadata();
    }

    public SPlayer(String variable, String schema, String table) {
        super(SPlayer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPlayer(Path<? extends SPlayer> path) {
        super(path.getType(), path.getMetadata(), "", "player_");
        addMetadata();
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata, "", "player_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

