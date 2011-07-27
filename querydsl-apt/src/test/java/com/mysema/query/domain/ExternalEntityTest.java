package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.codegen.sub.AbstractEntity;

@Ignore
public class ExternalEntityTest {

    @QueryEntity
    public class MyEntity extends AbstractEntity<MyEntity> {
        
    }
    
}
