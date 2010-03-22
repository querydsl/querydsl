/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("all")
@Table("SURVEY")
public class QSurvey extends PEntity<QSurvey> {
    
    public Expr<Object[]> all(){
        return CSimple.create(Object[].class, "{0}.*", this);
    }
        
    public final PString name = createString("NAME");
    
    public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);

    public QSurvey(java.lang.String path) {
        super(QSurvey.class, PathMetadataFactory.forVariable(path));
    }

    public QSurvey(PathMetadata<?> metadata) {
        super(QSurvey.class, metadata);
    }
}