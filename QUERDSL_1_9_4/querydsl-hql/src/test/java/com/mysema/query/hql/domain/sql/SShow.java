package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SShow is a Querydsl query type for SShow
 */
@Table("SHOW")
public class SShow extends BeanPath<SShow> implements RelationalPath<SShow> {

    private static final long serialVersionUID = 501219270;

    public static final SShow show = new SShow("SHOW");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    private Expr[] _all;

    public final PrimaryKey<SShow> sql100819184438080 = new PrimaryKey<SShow>(this, id);

    public final ForeignKey<SShowActs> _fk5f6ee0319084d04 = new ForeignKey<SShowActs>(this, id, "SHOW_ID");

    public SShow(String variable) {
        super(SShow.class, forVariable(variable));
    }

    public SShow(BeanPath<? extends SShow> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SShow(PathMetadata<?> metadata) {
        super(SShow.class, metadata);
    }

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id};
        }
        return _all;
    }

    public PrimaryKey<SShow> getPrimaryKey() {
        return sql100819184438080;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk5f6ee0319084d04);
    }

    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.<Expr<?>>asList(all());
    }
}

