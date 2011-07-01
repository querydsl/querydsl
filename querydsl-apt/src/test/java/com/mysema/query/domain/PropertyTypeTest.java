package com.mysema.query.domain;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryType;

@Ignore
public class PropertyTypeTest {

    @QueryEntity
    public class Entity {
        
        @QueryType(PropertyType.STRING)
        Integer numberAsString;
        
    }
    
    @Test
    public void NumberAsString_Like(){
        QPropertyTypeTest_Entity.entity.numberAsString.like("a");
    }
}
