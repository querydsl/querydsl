package com.mysema.query.codegen;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class EntityInheritanceTest extends AbstractExporterTest {
    
    @QuerySupertype
    public static class TreeEntity<T extends TreeEntity<T>> {
        
        public Integer id;
        
        public T parent;
        
    }

    @QueryEntity
    public static class TestEntity extends TreeEntity<TestEntity> {
        
        public String name;
        
    }
    
}
