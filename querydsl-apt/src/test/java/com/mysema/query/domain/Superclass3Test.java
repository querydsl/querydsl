package com.mysema.query.domain;

import org.junit.Assert;
import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.annotations.QueryEntity;

public class Superclass3Test {

    @QueryEntity
    public static class Subtype extends DefaultQueryMetadata{

        private static final long serialVersionUID = -218949941713252847L;
        
    }
    
    @Test
    public void test(){
        Assert.assertNotNull(QSuperclass3Test_Subtype.subtype.distinct);
    }
}
