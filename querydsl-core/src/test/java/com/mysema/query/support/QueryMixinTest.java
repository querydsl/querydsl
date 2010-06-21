/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.domain.QCommonPersistence;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PathMetadataFactory;

public class QueryMixinTest {

    private QueryMixin<?> mixin = new QueryMixin();

    private QCommonPersistence entity = new QCommonPersistence(PathMetadataFactory.forVariable("entity"));

    @Test
    public void getJoins_with_condition(){
        mixin.innerJoin(entity);
        mixin.on(entity.version.isNull(), entity.version.isNotNull());

        assertEquals(1, mixin.getMetadata().getJoins().size());
        JoinExpression je = mixin.getMetadata().getJoins().get(0);
        assertEquals(entity, je.getTarget());
        assertEquals(EBoolean.allOf(entity.version.isNull(), entity.version.isNotNull()), je.getCondition());
    }

    @Test
    public void getJoins_no_condition(){
        mixin.innerJoin(entity);

        assertEquals(1, mixin.getMetadata().getJoins().size());
        JoinExpression je = mixin.getMetadata().getJoins().get(0);
        assertEquals(entity, je.getTarget());
        assertNull(je.getCondition());
    }

}
