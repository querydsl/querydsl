/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Calendar;

import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;

public final class Constants {
    
    private Constants(){}

    public static final java.sql.Date date;
    
    public static final QEmployee employee = new QEmployee("e");
    
    public static final QEmployee employee2 = new QEmployee("e2");
    
    public static final QSurvey survey = new QSurvey("s");

    public static final QSurvey survey2 = new QSurvey("s2");
    
    public static final java.sql.Time time;
    
    static{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }
}
