package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SPersonid is a Querydsl query type for SPersonid
 */
@Table("PERSONID")
public class SPersonid extends BeanPath<SPersonid> implements RelationalPath<SPersonid> {

    private static final long serialVersionUID = 1500692345;

    public static final SPersonid personid = new SPersonid("PERSONID");

    public final StringPath country = createString("COUNTRY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    private Expression[] _all;

    public final PrimaryKey<SPersonid> sql100819184437170 = new PrimaryKey<SPersonid>(this, id);

    public final ForeignKey<SPerson> _fk8e48877578234709 = new ForeignKey<SPerson>(this, id, "PID_ID");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable));
    }

    public SPersonid(BeanPath<? extends SPersonid> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{country, id, medicarenumber};
        }
        return _all;
    }

    public PrimaryKey<SPersonid> getPrimaryKey() {
        return sql100819184437170;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk8e48877578234709);
    }
    
    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }

}

