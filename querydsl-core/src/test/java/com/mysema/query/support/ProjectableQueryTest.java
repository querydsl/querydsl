package com.mysema.query.support;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.QBean;

@SuppressWarnings("unchecked")
public class ProjectableQueryTest {
        
    @Test
    public void UniqueResult_Of_Array(){
       QueryMixin queryMixin = new QueryMixin();
       DummyProjectable projectable = new DummyProjectable(queryMixin);
       projectable.uniqueResult(new Expression[0]);
       assertEquals(Long.valueOf(2), queryMixin.getMetadata().getModifiers().getLimit());
    }
    
    @Test
    public void UniqueResult(){
       QueryMixin queryMixin = new QueryMixin();
       DummyProjectable projectable = new DummyProjectable(queryMixin);
       projectable.uniqueResult(new QBean(Object.class));
       assertEquals(Long.valueOf(2), queryMixin.getMetadata().getModifiers().getLimit());
    }

}
