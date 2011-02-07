/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.query.Detachable;


@SuppressWarnings("unchecked")
public class DetachableAdapterTest {
    
    private QueryMixin queryMixin;
    
    private Detachable detachable;
    
    @Before
    public void setUp(){
        queryMixin = new QueryMixin();
        detachable = new DetachableAdapter(new DetachableMixin(queryMixin));
    }
    
    @Test
    public void Projections(){
        DummyEntity e = Alias.alias(DummyEntity.class);
        queryMixin.from((EntityPath)$(e));
        assertNotNull(detachable.exists());
        assertNotNull(detachable.notExists());
        assertNotNull(detachable.list($(e)));
        assertNotNull(detachable.unique($(e)));
        assertNotNull(detachable.list($(e), $(e.getOther())));
        assertNotNull(detachable.unique($(e), $(e.getOther())));
    }

}
