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
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;

/**
 * SFormula is a Querydsl query type for SFormula
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="FORMULA")
public class SFormula extends PEntity<SFormula> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> parameterId = createNumber("PARAMETER_ID", Long.class);

    public SFormula(String variable) {
        super(SFormula.class, forVariable(variable));
    }

    public SFormula(PEntity<? extends SFormula> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SFormula(PathMetadata<?> metadata) {
        super(SFormula.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

