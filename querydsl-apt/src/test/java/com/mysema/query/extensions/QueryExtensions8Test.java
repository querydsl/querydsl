package com.mysema.query.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;

import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerydslConfig;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;

public class QueryExtensions8Test {
    
//  * A date period to be applied on java.sql.Date and other on java.sql.Timestamp (dates consider whole days, while timestamp is exact)
//  * Comparing dates with a TimeInterval (which has a number and a field - hours, days, months...)
//  * Checking whether a boolean column is false (could be either null or false)
//  * Comparing numbers with FileSize (which has a number and a field - bytes, KB, MB), where the original number is always in bytes
    
    
    @QueryDelegate(Date.class)
    public static BooleanExpression inPeriod(DatePath<Date> date, Pair<Date,Date> period){
        return date.goe(period.getFirst()).and(date.loe(period.getSecond()));
    }
    
    @QueryDelegate(Boolean.class)
    public static BooleanExpression isFalse1(BooleanPath expr){
        return expr.isNull().or(expr.eq(false));
    }
    
    @QueryDelegate(Boolean.class)
    public static BooleanExpression isTrue1(BooleanPath expr){
        return expr.isNotNull();
    }

    public static class FileSize {
        public int bytes = 1000;
    }
    
    @QueryDelegate(Integer.class)
    public static BooleanExpression eq(NumberPath<Integer> intVal, FileSize size){
        return intVal.eq(size.bytes);
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
            entity.booleanVal.isFalse1().toString());
        
        assertEquals(
                "entity.booleanVal is not null",
                entity.booleanVal.isTrue1().toString());
        
        assertEquals(
            "entity.intVal = 1000",
            entity.intVal.eq(new FileSize()).toString());
    }
    
}
