/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
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
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.TimePath;

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
        cl = QJodaTimeSupportTest_JodaTimeSupport.class;
        match(DateTimePath.class, "dateMidnight");
        match(DateTimePath.class, "dateTime");
        match(DateTimePath.class, "instant");
        match(DatePath.class, "localDate");
        match(DateTimePath.class, "localDateTime");
        match(TimePath.class, "localTime");
        match(ComparablePath.class, "partial");
    }
}
