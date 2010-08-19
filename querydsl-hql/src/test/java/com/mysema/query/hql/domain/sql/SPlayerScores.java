package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SPlayerScores is a Querydsl query type for SPlayerScores
 */
@Table("PLAYER_SCORES")
public class SPlayerScores extends BeanPath<SPlayerScores> implements RelationalPath<SPlayerScores> {

    private static final long serialVersionUID = -855115221;

    public static final SPlayerScores playerScores = new SPlayerScores("PLAYER_SCORES");

    public final PNumber<Integer> element = createNumber("ELEMENT", Integer.class);

    public final PNumber<Long> playerId = createNumber("PLAYER_ID", Long.class);

    private Expr[] _all;

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{element, playerId};
        }
        return _all;
    }

    public PrimaryKey<SPlayerScores> getPrimaryKey() {
        return null;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fkd5dc571ff51f2004);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

