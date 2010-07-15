package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.QByte;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;

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
    public static EBoolean isNumeric(QByte byteValue){
        return EBooleanConst.TRUE;
    }
    
    @Test
    public void test(){
        assertNotNull(QQueryExtensions7Test_Entity.entity.shortValue.isNumeric());
        assertNotNull(QQueryExtensions7Test_Entity.entity.byteValue.isNumeric());
    }

}
