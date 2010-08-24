package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SStatuschange is a Querydsl query type for SStatuschange
 */
@Table("STATUSCHANGE")
public class SStatuschange extends BeanPath<SStatuschange> implements RelationalPath<SStatuschange> {

    private static final long serialVersionUID = 1953690091;

    public static final SStatuschange statuschange = new SStatuschange("STATUSCHANGE");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PDateTime<Date> timestamp = createDateTime("TIMESTAMP", Date.class);

    private Expr[] _all;

    public final PrimaryKey<SStatuschange> sql100819184439140 = new PrimaryKey<SStatuschange>(this, id);

    public final ForeignKey<SItemStatuschange> _fkc2c9ebee2f721e35 = new ForeignKey<SItemStatuschange>(this, id, "STATUSCHANGES_ID");

    public SStatuschange(String variable) {
        super(SStatuschange.class, forVariable(variable));
    }

    public SStatuschange(BeanPath<? extends SStatuschange> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SStatuschange(PathMetadata<?> metadata) {
        super(SStatuschange.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, timestamp};
        }
        return _all;
    }

    public PrimaryKey<SStatuschange> getPrimaryKey() {
        return sql100819184439140;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fkc2c9ebee2f721e35);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

