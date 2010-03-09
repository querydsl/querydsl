package com.mysema.query;

import java.util.Calendar;

import com.mysema.query.sql.domain.QEMPLOYEE;
import com.mysema.query.sql.domain.QSURVEY;

public final class Constants {
    
    private Constants(){}

    static final java.sql.Date date;
    
    static final QEMPLOYEE employee = new QEMPLOYEE("e");
    
    static final QEMPLOYEE employee2 = new QEMPLOYEE("e2");
    
    static final QSURVEY survey = new QSURVEY("s");

    static final QSURVEY survey2 = new QSURVEY("s2");
    
    static final java.sql.Time time;
    
    static{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }
}
