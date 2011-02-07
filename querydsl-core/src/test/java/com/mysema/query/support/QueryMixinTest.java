/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.mysema.query.alias.Alias.*;

import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.alias.Alias;
import com.mysema.query.domain.QCommonPersistence;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.BooleanExpression;

public class QueryMixinTest {

    @SuppressWarnings("unchecked")
    private QueryMixin<?> mixin = new QueryMixin();

    private QCommonPersistence entity = new QCommonPersistence(PathMetadataFactory.forVariable("entity"));

    @Test
    public void GetJoins_with_condition(){
        mixin.innerJoin(entity);
        mixin.on(entity.version.isNull(), entity.version.isNotNull());

        assertEquals(1, mixin.getMetadata().getJoins().size());
        JoinExpression je = mixin.getMetadata().getJoins().get(0);
        assertEquals(entity, je.getTarget());
        assertEquals(BooleanExpression.allOf(entity.version.isNull(), entity.version.isNotNull()), je.getCondition());
    }

    @Test
    public void GetJoins_no_condition(){
        mixin.innerJoin(entity);

        assertEquals(1, mixin.getMetadata().getJoins().size());
        JoinExpression je = mixin.getMetadata().getJoins().get(0);
        assertEquals(entity, je.getTarget());
        assertNull(je.getCondition());
    }
    
    @Test
    public void ApplyJoins(){
        DummyEntity e = Alias.alias(DummyEntity.class);
        DummyEntity e2 = Alias.alias(DummyEntity.class, "e2");
        
        // inner join
        mixin.innerJoin($(e));
        mixin.innerJoin($(e.getOther()),$(e2));
        mixin.innerJoin($(e.getList()),$(e2));
        mixin.innerJoin($(e.getList()));
        mixin.innerJoin($(e.getMap()),$(e2));
        mixin.innerJoin($(e.getMap()));
        
        // join
        mixin.join($(e));
        mixin.join($(e.getOther()),$(e2));
        mixin.join($(e.getList()),$(e2));
        mixin.join($(e.getList()));
        mixin.join($(e.getMap()),$(e2));
        mixin.join($(e.getMap()));
        
        // left join
        mixin.leftJoin($(e));
        mixin.leftJoin($(e.getOther()),$(e2));
        mixin.leftJoin($(e.getList()),$(e2));
        mixin.leftJoin($(e.getList()));
        mixin.leftJoin($(e.getMap()),$(e2));
        mixin.leftJoin($(e.getMap()));
        
        // right join
        mixin.rightJoin($(e));
        mixin.rightJoin($(e.getOther()),$(e2));
        mixin.rightJoin($(e.getList()),$(e2));
        mixin.rightJoin($(e.getList()));
        mixin.rightJoin($(e.getMap()),$(e2));
        mixin.rightJoin($(e.getMap()));
        
        // full join
        mixin.fullJoin($(e));
        mixin.fullJoin($(e.getOther()),$(e2));
        mixin.fullJoin($(e.getList()),$(e2));
        mixin.fullJoin($(e.getList()));
        mixin.fullJoin($(e.getMap()),$(e2));
        mixin.fullJoin($(e.getMap()));
        
        assertEquals(6, mixin.getMetadata().getJoins().size());
    }

}
