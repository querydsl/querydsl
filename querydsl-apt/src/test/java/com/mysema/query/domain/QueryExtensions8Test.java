package com.mysema.query.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerydslConfig;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PNumber;

public class QueryExtensions8Test {
    
//  * A date period to be applied on java.sql.Date and other on java.sql.Timestamp (dates consider whole days, while timestamp is exact)
//  * Comparing dates with a TimeInterval (which has a number and a field - hours, days, months...)
//  * Checking whether a boolean column is false (could be either null or false)
//  * Comparing numbers with FileSize (which has a number and a field - bytes, KB, MB), where the original number is always in bytes
    
    
    @QueryDelegate(Date.class)
    public static EBoolean inPeriod(PDate<Date> date, Pair<Date,Date> period){
        return date.goe(period.getFirst()).and(date.loe(period.getSecond()));
    }
    
    @QueryDelegate(Boolean.class)
    public static EBoolean isFalse(PBoolean expr){
        return expr.isNull().or(expr.eq(false));
    }
    
    @QueryDelegate(Boolean.class)
    public static EBoolean isTrue(PBoolean expr){
        return expr.isNotNull();
    }

    public static class FileSize {
        public int bytes = 1000;
    }
    
    @QueryDelegate(Integer.class)
    public static EBoolean eq(PNumber<Integer> intValue, FileSize size){
        return intValue.eq(size.bytes);
    }
    

    @QueryEntity
    @QuerydslConfig(entityAccessors=true)
    public static class Entity {
        
        Entity superior;
        
        Date dateVal;
        
        Boolean booleanVal;
        
        Integer intVal;
        
    }
    
    @Test
    public void test(){
        QQueryExtensions8Test_Entity entity = QQueryExtensions8Test_Entity.entity;
        
        assertNotNull(entity.dateVal.inPeriod(Pair.of(new Date(0), new Date(0))));
//        assertEquals(
//            "entity.dateVal >= 1970-01-01 && entity.dateVal <= 1970-01-01",
//            entity.dateVal.inPeriod(Pair.of(new Date(0), new Date(0))).toString());
        
        assertEquals(
            "entity.booleanVal is null || entity.booleanVal = false",
            entity.booleanVal.isFalse().toString());
        
        assertEquals(
                "entity.booleanVal is not null",
                entity.booleanVal.isTrue().toString());
        
        assertEquals(
            "entity.intVal = 1000",
            entity.intVal.eq(new FileSize()).toString());
    }
    
}
