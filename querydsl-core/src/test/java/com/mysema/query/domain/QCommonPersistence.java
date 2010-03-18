/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * QCommonPersistence is a Querydsl query type for CommonPersistence
 */
public class QCommonPersistence extends PEntity<CommonPersistence> {

    private static final long serialVersionUID = -1494672641;

    public final PNumber<Long> version = createNumber("version", Long.class);

    public QCommonPersistence(PEntity<? extends CommonPersistence> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QCommonPersistence(PathMetadata<?> metadata) {
        super(CommonPersistence.class, metadata);
    }

}

