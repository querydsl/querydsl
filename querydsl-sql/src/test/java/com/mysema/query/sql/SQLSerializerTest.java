/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.domain.QSurvey;

public class SQLSerializerTest {

    @Test
    public void StartsWith(){
        SQLSerializer serializer = new SQLSerializer(SQLTemplates.DEFAULT);
        QSurvey s1 = new QSurvey("s1");
        serializer.handle(s1.name.startsWith("X"));
        assertEquals("s1.NAME like ?", serializer.toString());
        assertEquals(Arrays.asList("X%"), serializer.getConstants());
    }
    
    @Test
    public void Boolean(){
        QSurvey s = new QSurvey("s");
        BooleanBuilder bb1 = new BooleanBuilder();
        bb1.and(s.name.eq(s.name));

        BooleanBuilder bb2 = new BooleanBuilder();
        bb2.or(s.name.eq(s.name));
        bb2.or(s.name.eq(s.name));

        String str = new SQLSerializer(SQLTemplates.DEFAULT).handle(bb1.and(bb2)).toString();
        assertEquals("s.NAME = s.NAME and (s.NAME = s.NAME or s.NAME = s.NAME)", str);
    }
    
}
