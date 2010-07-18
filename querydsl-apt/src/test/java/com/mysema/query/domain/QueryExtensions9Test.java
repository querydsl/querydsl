package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerydslConfig;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;

public class QueryExtensions9Test {
    
//  * A date period to be applied on java.sql.Date and other on java.sql.Timestamp (dates consider whole days, while timestamp is exact)
//  * Comparing dates with a TimeInterval (which has a number and a field - hours, days, months...)
//  * Checking whether a boolean column is false (could be either null or false)
//  * Comparing numbers with FileSize (which has a number and a field - bytes, KB, MB), where the original number is always in bytes
    
    
    @QueryDelegate(Date.class)
    public static EBoolean inPeriod(PDate<Date> date, Pair<Date,Date> period){
        return date.goe(period.getFirst()).and(date.loe(period.getSecond()));
    }

    @QueryDelegate(Timestamp.class)
    public static EBoolean inDatePeriod(PDateTime<Timestamp> timestamp, Pair<Date,Date> period){
        Timestamp first = new Timestamp(DateUtils.truncate(period.getFirst(), Calendar.DAY_OF_MONTH).getTime());
        Calendar second = Calendar.getInstance();
        second.setTime(DateUtils.truncate(period.getSecond(), Calendar.DAY_OF_MONTH));
        second.add(1, Calendar.DAY_OF_MONTH);
        return timestamp.goe(first).and(timestamp.lt(new Timestamp(second.getTimeInMillis())));
    }

    @QueryDelegate(Boolean.class)
    public static EBoolean isFalse2(PBoolean expr){
        return expr.isNull().or(expr.eq(false));
    }
    
    @QueryDelegate(Boolean.class)
    public static EBoolean isTrue2(PBoolean expr){
        return expr.isNotNull().and(expr.eq(true));
    }

    public static class FileSize {
        public int bytes = 1000;
    }
    
    @QueryDelegate(Integer.class)
    public static EBoolean atMost(PNumber<Integer> intValue, FileSize size){
        return intValue.loe(size.bytes);
    }
    

    @QueryEntity
    @QuerydslConfig(entityAccessors=true)
    public static class Entity {
        
        Entity superior;
        
        Date dateVal;

        Timestamp timestampVal;

        Boolean booleanVal;
        
        Integer intVal;
        
    }
    
    @Test
    public void test(){
        QQueryExtensions9Test_Entity entity = QQueryExtensions9Test_Entity.entity;
        
        Date date1 = new Date(new GregorianCalendar(2010, 0, 1).getTimeInMillis());
        Date date2 = new Date(new GregorianCalendar(2010, 0, 2).getTimeInMillis());
        
        assertEquals(
            "entity.dateVal >= 2010-01-01 && entity.dateVal <= 2010-01-02",
            entity.dateVal.inPeriod(Pair.of(date1, date2)).toString());
        
        assertEquals(
            "entity.timestampVal >= 2010-01-01 00:00:00.0 && entity.timestampVal < 2015-01-02 00:00:00.0",
            entity.timestampVal.inDatePeriod(Pair.of(date1, date2)).toString());
        
        assertEquals(
            "entity.booleanVal is null || entity.booleanVal = false",
            entity.booleanVal.isFalse2().toString());
        
        assertEquals(
                "entity.booleanVal is not null && entity.booleanVal = true",
                entity.booleanVal.isTrue2().toString());
        
        assertEquals(
            "entity.intVal <= 1000",
            entity.intVal.atMost(new FileSize()).toString());
    }
    
}
