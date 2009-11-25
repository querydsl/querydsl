package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class SuperclassTest {
    
    @QuerySupertype
    public class SuperclassTestSuperclass{
        
    }
    
    @QuerySupertype
    public class SuperclassTestSuperclass2{
        SuperclassTestEntity ref;
    }
    
    @QueryEntity
    public class SuperclassTestEntity{
        SuperclassTestSuperclass ref;
        SuperclassTestSuperclass2 ref2;
    }

    @Test
    public void test(){
        // TODO
    }
}
