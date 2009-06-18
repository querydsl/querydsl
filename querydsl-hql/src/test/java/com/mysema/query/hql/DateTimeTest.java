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
        catalog.effectiveDate.getSeconds();
        catalog.effectiveDate.getMinutes();
        catalog.effectiveDate.getHours();
        catalog.effectiveDate.getDayOfMonth();
        catalog.effectiveDate.getMonth();
        catalog.effectiveDate.getYear();
    }

}
