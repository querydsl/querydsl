/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;


@Table("SURVEY")
public class QSurvey extends PEntity<QSurvey> {

    private static final long serialVersionUID = -7427577079709192842L;

    public final PString name = createString("NAME");

    public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);

    private final Expr<?>[] all = new Expr[]{id, name};

    public Expr<?>[] all() {
        return all;
    }
    
    public final PrimaryKey<QSurvey> idKey = new PrimaryKey<QSurvey>(this,id);

    public QSurvey(java.lang.String path) {
        super(QSurvey.class, PathMetadataFactory.forVariable(path));
    }

    public QSurvey(PathMetadata<?> metadata) {
        super(QSurvey.class, metadata);
    }
}
