/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;

/**
 * SSimpletypes is a Querydsl query type for SSimpletypes
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SIMPLETYPES")
public class SSimpletypes extends PEntity<SSimpletypes> {

    public final PNumber<Short> bbyte = createNumber("BBYTE", Short.class);

    public final PNumber<Short> bbyte2 = createNumber("BBYTE2", Short.class);

    public final PNumber<java.math.BigDecimal> bigdecimal = createNumber("BIGDECIMAL", java.math.BigDecimal.class);

    public final PComparable<Character> cchar = createComparable("CCHAR", Character.class);

    public final PComparable<Character> cchar2 = createComparable("CCHAR2", Character.class);

    public final PComparable<java.util.Date> date = createComparable("DATE", java.util.Date.class);

    public final PNumber<Double> ddouble = createNumber("DDOUBLE", Double.class);

    public final PNumber<Double> ddouble2 = createNumber("DDOUBLE2", Double.class);

    public final PNumber<Double> ffloat = createNumber("FFLOAT", Double.class);

    public final PNumber<Double> ffloat2 = createNumber("FFLOAT2", Double.class);

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> iint = createNumber("IINT", Integer.class);

    public final PNumber<Integer> iint2 = createNumber("IINT2", Integer.class);

    public final PString llocale = createString("LLOCALE");

    public final PNumber<Long> llong = createNumber("LLONG", Long.class);

    public final PNumber<Long> llong2 = createNumber("LLONG2", Long.class);

    public final PString sstring = createString("SSTRING");

    public final PComparable<java.sql.Time> time = createComparable("TIME", java.sql.Time.class);

    public final PComparable<java.util.Date> timestamp = createComparable("TIMESTAMP", java.util.Date.class);

    public SSimpletypes(String variable) {
        super(SSimpletypes.class, forVariable(variable));
    }

    public SSimpletypes(PEntity<? extends SSimpletypes> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SSimpletypes(PathMetadata<?> metadata) {
        super(SSimpletypes.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

