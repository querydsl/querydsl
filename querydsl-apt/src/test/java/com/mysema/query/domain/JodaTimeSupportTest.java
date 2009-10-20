package com.mysema.query.domain;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Partial;
import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PTime;

public class JodaTimeSupportTest extends AbstractTest{
    
    @QueryEntity
    public static class JodaTimeSupport {

        DateMidnight dateMidnight;

        DateTime dateTime;

        Instant instant;

        LocalDate localDate;

        LocalDateTime localDateTime;

        LocalTime localTime;

        Partial partial;

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        cl = QJodaTimeSupport.class;
        match(PDateTime.class, "dateMidnight");
        match(PDateTime.class, "dateTime");
        match(PDateTime.class, "instant");
        match(PDate.class, "localDate");
        match(PDateTime.class, "localDateTime");
        match(PTime.class, "localTime");
        match(PComparable.class, "partial");
    }
}
