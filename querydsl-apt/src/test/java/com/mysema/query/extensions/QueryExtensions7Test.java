package com.mysema.query.extensions;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanConstant;

import ext.java.lang.QByte;

@Ignore
public class QueryExtensions7Test {
    
    @QueryEntity
    public static class Entity {
        
        Short shortValue;
        
        Byte byteValue;
        
    }
    
    @QueryExtensions(Short.class)
    public static interface ShortMethods{

        @QueryMethod("isNumeric({0})")
        boolean isNumeric();

    }
    
    @QueryDelegate(Byte.class)
    public static BooleanExpression isNumeric(QByte byteValue){
        return BooleanConstant.TRUE;
    }
    
    @Test
    public void test(){
        assertNotNull(QQueryExtensions7Test_Entity.entity.shortValue.isNumeric());
        assertNotNull(QQueryExtensions7Test_Entity.entity.byteValue.isNumeric());
    }

}
