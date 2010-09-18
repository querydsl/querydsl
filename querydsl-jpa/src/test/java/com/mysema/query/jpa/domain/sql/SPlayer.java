package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@Table("PLAYER")
public class SPlayer extends RelationalPathBase<SPlayer> implements RelationalPath<SPlayer> {

    private static final long serialVersionUID = 552776042;

    public static final SPlayer player = new SPlayer("PLAYER");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SPlayer> sql100819184437420 = createPrimaryKey(id);

    public final ForeignKey<SPlayerScores> _fkd5dc571ff51f2004 = new ForeignKey<SPlayerScores>(this, id, "PLAYER_ID");

    public SPlayer(String variable) {
        super(SPlayer.class, forVariable(variable));
    }

    public SPlayer(BeanPath<? extends SPlayer> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPlayer(PathMetadata<?> metadata) {
        super(SPlayer.class, metadata);
    }

}

