package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.rel.SimpleType;
import com.mysema.query.domain.rel.SimpleType2;

public class GenericTest {
    
    @QueryEntity
    public class GenericType<T extends ItemType> {
        T itemType;
    }
    
    @QueryEntity
    public class GenericType2<T extends ItemType> {
        T itemType;
    }
    
    @QueryEntity
    public class ItemType {    
        Amount<SimpleType> prop;    
        SimpleType2<Amount<SimpleType>> prop2;
        @SuppressWarnings("unchecked")
        SimpleType2<Amount> prop3;
        SimpleType2<?> prop4;
    }
    
    public class Amount<T>{
        
    }

    @Test
    public void test(){
        // TODO
    }
}
