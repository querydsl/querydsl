package com.mysema.query.sql.oracle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.domain.QSurvey;


public class OracleSerializerTest {

    @Test
    public void testBoolean(){
        QSurvey s = new QSurvey("s");
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(s.name.eq(s.name));
       
        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(s.name.eq(s.name));
        bb2.or(s.name.eq(s.name));
       
        String str = new OracleSerializer(SQLTemplates.DEFAULT, null, null, null, null, null).handle(bb1.and(bb2)).toString();
        assertEquals("s.NAME = s.NAME and (s.NAME = s.NAME or s.NAME = s.NAME)", str);
    }
    
}
