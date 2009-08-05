package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ETime;

public class DateTimeTest extends AbstractQueryTest {
    
    @Test
    public void testDateOperations() {
        // current_date(), current_time(), current_timestamp()
        toString("current_date()", EDate.currentDate());
        toString("current_date()", EDateTime.currentDate());
        toString("current_time()", ETime.currentTime());
        toString("current_timestamp()", EDateTime.currentTimestamp());
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
        catalog.effectiveDate.getSeconds();
        catalog.effectiveDate.getMinutes();
        catalog.effectiveDate.getHours();
        catalog.effectiveDate.getDayOfMonth();
        catalog.effectiveDate.getMonth();
        catalog.effectiveDate.getYear();
    }

}
