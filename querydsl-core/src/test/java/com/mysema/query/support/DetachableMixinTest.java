package com.mysema.query.support;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.EntityPath;


public class DetachableMixinTest {
    
    private QueryMixin query;
    
    private DetachableMixin detachable;
    
    @Before
    public void setUp(){
        query = new QueryMixin();
        detachable = new DetachableMixin(query);
    }
    
    @Test
    public void testProjections(){
        DummyEntity e = Alias.alias(DummyEntity.class);
        query.from((EntityPath)$(e));
        assertNotNull(detachable.exists());
        assertNotNull(detachable.notExists());
        assertNotNull(detachable.list($(e)));
        assertNotNull(detachable.unique($(e)));
        assertNotNull(detachable.list($(e), $(e.getOther())));
        assertNotNull(detachable.unique($(e), $(e.getOther())));
    }

}
