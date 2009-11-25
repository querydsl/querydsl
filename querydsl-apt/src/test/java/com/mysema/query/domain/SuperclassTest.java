package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class SuperclassTest {
    
    @QuerySupertype
    public class SuperclassTestSuperclass{
        
    }
    
    @QueryEntity
    public class SuperclassTestEntity{
        SuperclassTestSuperclass ref;
    }

    @Test
    public void test(){
        // TODO
    }
}
