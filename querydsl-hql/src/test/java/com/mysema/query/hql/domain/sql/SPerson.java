package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SPerson is a Querydsl query type for SPerson
 */
@Table("PERSON")
public class SPerson extends BeanPath<SPerson> implements RelationalPath<SPerson> {

    private static final long serialVersionUID = 546812382;

    public static final SPerson person = new SPerson("PERSON");

    public final DatePath<java.sql.Date> birthday = createDate("BIRTHDAY", java.sql.Date.class);

    public final NumberPath<Long> i = createNumber("I", Long.class);

    public final StringPath name = createString("NAME");

    public final NumberPath<Long> nationalityId = createNumber("NATIONALITY_ID", Long.class);

    public final NumberPath<Long> pidId = createNumber("PID_ID", Long.class);

    private Expression[] _all;

    public final PrimaryKey<SPerson> sql100819184436900 = new PrimaryKey<SPerson>(this, i);

    public final ForeignKey<SNationality> fk8e488775e9d94490 = new ForeignKey<SNationality>(this, nationalityId, "ID");

    public final ForeignKey<SPersonid> fk8e48877578234709 = new ForeignKey<SPersonid>(this, pidId, "ID");

    public final ForeignKey<SAccount> _fk1d0c220d257b5f1c = new ForeignKey<SAccount>(this, i, "OWNER_I");

    public SPerson(String variable) {
        super(SPerson.class, forVariable(variable));
    }

    public SPerson(BeanPath<? extends SPerson> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPerson(PathMetadata<?> metadata) {
        super(SPerson.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{birthday, i, name, nationalityId, pidId};
        }
        return _all;
    }

    public PrimaryKey<SPerson> getPrimaryKey() {
        return sql100819184436900;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk8e488775e9d94490, fk8e48877578234709);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk1d0c220d257b5f1c);
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }
}

