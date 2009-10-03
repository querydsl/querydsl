/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.domain;

import static org.junit.Assert.assertTrue;

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

/**
 * @author tiwe
 * 
 */
@QueryEntity
public class JodaTimeSupport {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        QJodaTimeSupport i = QJodaTimeSupport.jodaTimeSupport;
        assertTrue(i.dateMidnight instanceof PDateTime);
        assertTrue(i.dateTime instanceof PDateTime);
        assertTrue(i.instant instanceof PDateTime);
        assertTrue(i.localDate instanceof PDate);
        assertTrue(i.localDateTime instanceof PDateTime);
        assertTrue(i.localTime instanceof PTime);
        assertTrue(i.partial instanceof PComparable);
    }

    DateMidnight dateMidnight;

    DateTime dateTime;

    Instant instant;

    LocalDate localDate;

    LocalDateTime localDateTime;

    LocalTime localTime;

    Partial partial;

}
