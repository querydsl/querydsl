/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PNumber;

/**
 * QCommonPersistence is a Querydsl query type for CommonPersistence
 */
public class QCommonPersistence extends BeanPath<CommonPersistence> implements EntityPath<CommonPersistence> {

    private static final long serialVersionUID = -1494672641;

    public final PNumber<Long> version = createNumber("version", Long.class);

    public QCommonPersistence(BeanPath<? extends CommonPersistence> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QCommonPersistence(PathMetadata<?> metadata) {
        super(CommonPersistence.class, metadata);
    }

}

