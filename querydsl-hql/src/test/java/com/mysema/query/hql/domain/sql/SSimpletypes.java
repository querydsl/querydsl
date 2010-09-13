package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SSimpletypes is a Querydsl query type for SSimpletypes
 */
@Table("SIMPLETYPES")
public class SSimpletypes extends BeanPath<SSimpletypes> implements RelationalPath<SSimpletypes> {

    private static final long serialVersionUID = -171311842;

    public static final SSimpletypes simpletypes = new SSimpletypes("SIMPLETYPES");

    public final NumberPath<Short> bbyte = createNumber("BBYTE", Short.class);

    public final NumberPath<Short> bbyte2 = createNumber("BBYTE2", Short.class);

    public final NumberPath<java.math.BigDecimal> bigdecimal = createNumber("BIGDECIMAL", java.math.BigDecimal.class);

    public final StringPath cchar = createString("CCHAR");

    public final StringPath cchar2 = createString("CCHAR2");

    public final DatePath<java.sql.Date> date = createDate("DATE", java.sql.Date.class);

    public final NumberPath<Double> ddouble = createNumber("DDOUBLE", Double.class);

    public final NumberPath<Double> ddouble2 = createNumber("DDOUBLE2", Double.class);

    public final NumberPath<Double> ffloat = createNumber("FFLOAT", Double.class);

    public final NumberPath<Double> ffloat2 = createNumber("FFLOAT2", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> iint = createNumber("IINT", Integer.class);

    public final NumberPath<Integer> iint2 = createNumber("IINT2", Integer.class);

    public final StringPath llocale = createString("LLOCALE");

    public final NumberPath<Long> llong = createNumber("LLONG", Long.class);

    public final NumberPath<Long> llong2 = createNumber("LLONG2", Long.class);

    public final StringPath sstring = createString("SSTRING");

    public final TimePath<java.sql.Time> time = createTime("TIME", java.sql.Time.class);

    public final DateTimePath<Date> timestamp = createDateTime("TIMESTAMP", Date.class);

    private Expression[] _all;

    public final PrimaryKey<SSimpletypes> sql100819184438610 = new PrimaryKey<SSimpletypes>(this, id);

    public SSimpletypes(String variable) {
        super(SSimpletypes.class, forVariable(variable));
    }

    public SSimpletypes(BeanPath<? extends SSimpletypes> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SSimpletypes(PathMetadata<?> metadata) {
        super(SSimpletypes.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{bbyte, bbyte2, bigdecimal, cchar, cchar2, date, ddouble, ddouble2, ffloat, ffloat2, id, iint, iint2, llocale, llong, llong2, sstring, time, timestamp};
        }
        return _all;
    }

    public PrimaryKey<SSimpletypes> getPrimaryKey() {
        return sql100819184438610;
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

