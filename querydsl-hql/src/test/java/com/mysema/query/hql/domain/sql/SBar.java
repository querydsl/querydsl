package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SBar is a Querydsl query type for SBar
 */
@Table("BAR")
public class SBar extends BeanPath<SBar> implements RelationalPath<SBar> {

    private static final long serialVersionUID = 1401625130;

    public static final SBar bar = new SBar("BAR");

    public final DatePath<java.sql.Date> date = createDate("DATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    private Expression[] _all;

    public final PrimaryKey<SBar> sql100819184430740 = new PrimaryKey<SBar>(this, id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable));
    }

    public SBar(BeanPath<? extends SBar> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{date, id};
        }
        return _all;
    }

    public PrimaryKey<SBar> getPrimaryKey() {
        return sql100819184430740;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

