package com.mysema.query.support;

import org.junit.Test;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;

public class QueryMixinPerformanceTest {

    @Test
    public void Normal() { // 1791
        EntityPath<DummyEntity> entity = new EntityPathBase<DummyEntity>(DummyEntity.class, "entity");
        EntityPathBase<DummyEntity> other = new EntityPathBase<DummyEntity>(DummyEntity.class, 
                PathMetadataFactory.forProperty(entity, "other"));
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            QueryMixin mixin = new QueryMixin();
            mixin.from(entity);
            mixin.where(other.eq(new DummyEntity()));
            mixin.addToProjection(entity);
        }
        System.err.println(System.currentTimeMillis() - start);
    }
    
    @Test
    public void Array_Arguments() { // 1125
        EntityPath<DummyEntity> entity = new EntityPathBase<DummyEntity>(DummyEntity.class, "entity");
        EntityPath[] entities = new EntityPath[]{entity};
        EntityPathBase<DummyEntity> other = new EntityPathBase<DummyEntity>(DummyEntity.class, 
                PathMetadataFactory.forProperty(entity, "other"));
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            QueryMixin mixin = new QueryMixin();
            mixin.from(entities);
            mixin.where(other.eq(new DummyEntity()));
            mixin.addToProjection(entities);
        }
        System.err.println(System.currentTimeMillis() - start);
    }
    
}
