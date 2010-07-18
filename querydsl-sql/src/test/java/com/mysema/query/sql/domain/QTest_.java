/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("TEST")
public class QTest_ extends PEntity<java.lang.Object> {

    private static final long serialVersionUID = -8421112749591552595L;

    public Expr<Object[]> all(){
        return CSimple.create(Object[].class, "{0}.*", this);
    }

    public final PString name = createString("NAME");

    public QTest_(java.lang.String path) {
        super(java.lang.Object.class, PathMetadataFactory.forVariable(path));
    }

    public QTest_(PathMetadata<?> metadata) {
        super(java.lang.Object.class, metadata);
    }
}
