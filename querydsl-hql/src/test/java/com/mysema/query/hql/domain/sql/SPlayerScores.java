package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@Table("PLAYER_SCORES")
public class SPlayerScores extends RelationalPathBase<SPlayerScores> implements RelationalPath<SPlayerScores> {

    private static final long serialVersionUID = -855115221;

    public static final SPlayerScores playerScores = new SPlayerScores("PLAYER_SCORES");

    public final NumberPath<Integer> element = createNumber("ELEMENT", Integer.class);

    public final NumberPath<Long> playerId = createNumber("PLAYER_ID", Long.class);

    public final ForeignKey<SPlayer> fkd5dc571ff51f2004 = new ForeignKey<SPlayer>(this, playerId, "ID");

    public SPlayerScores(String variable) {
        super(SPlayerScores.class, forVariable(variable));
    }

    public SPlayerScores(BeanPath<? extends SPlayerScores> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPlayerScores(PathMetadata<?> metadata) {
        super(SPlayerScores.class, metadata);
    }

}

