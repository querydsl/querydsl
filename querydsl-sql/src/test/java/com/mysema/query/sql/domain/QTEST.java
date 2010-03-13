/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.sql.Table;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("all")
@Table("test")
public class QTEST extends PEntity<java.lang.Object> {
    
    public Expr<Object[]> all(){
        return CSimple.create(Object[].class, "{0}.*", this);
    }
        
    public final PString name = createString("name");

    public QTEST(java.lang.String path) {
        super(java.lang.Object.class, PathMetadataFactory.forVariable(path));
    }

    public QTEST(PathMetadata<?> metadata) {
        super(java.lang.Object.class, metadata);
    }
}