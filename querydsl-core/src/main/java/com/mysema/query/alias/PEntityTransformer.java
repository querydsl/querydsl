/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import org.apache.commons.collections15.Transformer;

import com.mysema.commons.lang.Pair;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;

/**
 * @author tiwe
 *
 */
public final class PEntityTransformer implements Transformer<Pair<Class<?>, String>, EntityPathBase<?>> {

    @SuppressWarnings("unchecked")
    @Override
    public EntityPathBase<?> transform(Pair<Class<?>, String> input) {
        return new EntityPathBase(input.getFirst(), PathMetadataFactory.forVariable(input.getSecond()));
    }

}
