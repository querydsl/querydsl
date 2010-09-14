/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;

/**
 * SKittensArray is a Querydsl query type for SKittensArray
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS_ARRAY")
public class SKittensArray extends RelationalPathBase<SKittensArray> {

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> arrayindex = createNumber("ARRAYINDEX", Integer.class);

    public final NumberPath<Integer> kittensarrayId = createNumber("KITTENSARRAY_ID", Integer.class);

    public SKittensArray(String variable) {
        super(SKittensArray.class, forVariable(variable));
    }

    public SKittensArray(BeanPath<? extends SKittensArray> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittensArray(PathMetadata<?> metadata) {
        super(SKittensArray.class, metadata);
    }

}

