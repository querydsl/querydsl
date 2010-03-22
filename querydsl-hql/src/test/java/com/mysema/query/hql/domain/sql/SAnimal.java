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
 * SAnimal is a Querydsl query type for SAnimal
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ANIMAL")
public class SAnimal extends PEntity<SAnimal> {

    public final PNumber<Short> alive = createNumber("ALIVE", Short.class);

    public final PComparable<java.util.Date> birthdate = createComparable("BIRTHDATE", java.util.Date.class);

    public final PNumber<Double> bodyweight = createNumber("BODYWEIGHT", Double.class);

    public final PNumber<Integer> breed = createNumber("BREED", Integer.class);

    public final PNumber<Integer> color = createNumber("COLOR", Integer.class);

    public final PComparable<java.util.Date> datefield = createComparable("DATEFIELD", java.util.Date.class);

    public final PString dtype = createString("DTYPE");

    public final PNumber<Integer> eyecolor = createNumber("EYECOLOR", Integer.class);

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Integer> mateId = createNumber("MATE_ID", Integer.class);

    public final PString name = createString("NAME");

    public final PComparable<java.sql.Time> timefield = createComparable("TIMEFIELD", java.sql.Time.class);

    public final PNumber<Integer> toes = createNumber("TOES", Integer.class);

    public final PNumber<Integer> weight = createNumber("WEIGHT", Integer.class);

    public SAnimal(String variable) {
        super(SAnimal.class, forVariable(variable));
    }

    public SAnimal(PEntity<? extends SAnimal> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SAnimal(PathMetadata<?> metadata) {
        super(SAnimal.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

