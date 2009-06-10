package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.functions.DateTimeFunctions;

public class DateTimeTest extends AbstractQueryTest {
    
    @Test
    public void testDateOperations() {
        // current_date(), current_time(), current_timestamp()
        toString("current_date()", DateTimeFunctions.currentDate());
        toString("current_time()", DateTimeFunctions.currentTime());
        toString("current_timestamp()", DateTimeFunctions.currentTimestamp());
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
        DateTimeFunctions.seconds(catalog.effectiveDate);
        DateTimeFunctions.minutes(catalog.effectiveDate);
        DateTimeFunctions.hours(catalog.effectiveDate);
        DateTimeFunctions.dayOfMonth(catalog.effectiveDate);
        DateTimeFunctions.month(catalog.effectiveDate);
        DateTimeFunctions.year(catalog.effectiveDate);
    }

}
