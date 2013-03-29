package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.types.Predicate;

public class JPAQueryMixinTest {
    
    private JPAQueryMixin mixin = new JPAQueryMixin();

    @Test
    public void Where_Null() {
        mixin.where((Predicate)null);
    }
    

}
