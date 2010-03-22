/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;

/**
 * SDocument is a Querydsl query type for SDocument
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="DOCUMENT")
public class SDocument extends PEntity<SDocument> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public final PComparable<java.util.Date> validto = createComparable("VALIDTO", java.util.Date.class);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable));
    }

    public SDocument(PEntity<? extends SDocument> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

