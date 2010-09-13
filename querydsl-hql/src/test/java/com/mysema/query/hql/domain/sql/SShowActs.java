package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@Table("SHOW_ACTS")
public class SShowActs extends BeanPath<SShowActs> implements RelationalPath<SShowActs> {

    private static final long serialVersionUID = 718125831;

    public static final SShowActs showActs = new SShowActs("SHOW_ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final StringPath element = createString("ELEMENT");

    public final NumberPath<Integer> showId = createNumber("SHOW_ID", Integer.class);

    private Expression[] _all;

    public final PrimaryKey<SShowActs> sql100819184438340 = new PrimaryKey<SShowActs>(this, actsKey, showId);

    public final ForeignKey<SShow> fk5f6ee0319084d04 = new ForeignKey<SShow>(this, showId, "ID");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable));
    }

    public SShowActs(BeanPath<? extends SShowActs> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{actsKey, element, showId};
        }
        return _all;
    }

    public PrimaryKey<SShowActs> getPrimaryKey() {
        return sql100819184438340;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk5f6ee0319084d04);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

