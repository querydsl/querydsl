package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SFoo is a Querydsl query type for SFoo
 */
@Table("FOO")
public class SFoo extends BeanPath<SFoo> implements RelationalPath<SFoo> {

    private static final long serialVersionUID = 1401629405;

    public static final SFoo foo = new SFoo("FOO");

    public final PString bar = createString("BAR");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PDate<java.sql.Date> startdate = createDate("STARTDATE", java.sql.Date.class);

    private Expr[] _all;

    public final PrimaryKey<SFoo> sql100819184433460 = new PrimaryKey<SFoo>(this, id);

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable));
    }

    public SFoo(BeanPath<? extends SFoo> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{bar, id, startdate};
        }
        return _all;
    }

    public PrimaryKey<SFoo> getPrimaryKey() {
        return sql100819184433460;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

