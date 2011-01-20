package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SNamelist is a Querydsl query type for SNamelist
 */
@Table("NAMELIST")
public class SNamelist extends BeanPath<SNamelist> implements RelationalPath<SNamelist> {

    private static final long serialVersionUID = -1396144654;

    public static final SNamelist namelist = new SNamelist("NAMELIST");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    private Expr[] _all;

    public final PrimaryKey<SNamelist> sql100819184435410 = new PrimaryKey<SNamelist>(this, id);

    public final ForeignKey<SNamelistNames> _fkd6c82d72b8406ca4 = new ForeignKey<SNamelistNames>(this, id, "NAMELIST_ID");

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable));
    }

    public SNamelist(BeanPath<? extends SNamelist> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id};
        }
        return _all;
    }

    public PrimaryKey<SNamelist> getPrimaryKey() {
        return sql100819184435410;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fkd6c82d72b8406ca4);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

