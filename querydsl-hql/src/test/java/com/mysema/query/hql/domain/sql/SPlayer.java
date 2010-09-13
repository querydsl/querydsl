package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SPlayer is a Querydsl query type for SPlayer
 */
@Table("PLAYER")
public class SPlayer extends BeanPath<SPlayer> implements RelationalPath<SPlayer> {

    private static final long serialVersionUID = 552776042;

    public static final SPlayer player = new SPlayer("PLAYER");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    private Expression[] _all;

    public final PrimaryKey<SPlayer> sql100819184437420 = new PrimaryKey<SPlayer>(this, id);

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

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{id};
        }
        return _all;
    }

    public PrimaryKey<SPlayer> getPrimaryKey() {
        return sql100819184437420;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fkd5dc571ff51f2004);
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

