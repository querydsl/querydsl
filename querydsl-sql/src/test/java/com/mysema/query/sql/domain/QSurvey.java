/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expr;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;


@Table("SURVEY")
public class QSurvey extends BeanPath<QSurvey> implements RelationalPath<QSurvey>{

    private static final long serialVersionUID = -7427577079709192842L;

    public final PString name = createString("NAME");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    private final Expr<?>[] all = new Expr[]{id, name};

    public Expr<?>[] all() {
        return all;
    }
    
    public final PrimaryKey<QSurvey> idKey = new PrimaryKey<QSurvey>(this,id);

    public QSurvey(String path) {
        super(QSurvey.class, PathMetadataFactory.forVariable(path));
    }

    public QSurvey(PathMetadata<?> metadata) {
        super(QSurvey.class, metadata);
    }
    
    @Override
    public List<Expr<?>> getColumns() {
        return Arrays.asList(all);
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public PrimaryKey<QSurvey> getPrimaryKey() {
        return idKey; 
    }
    
}
