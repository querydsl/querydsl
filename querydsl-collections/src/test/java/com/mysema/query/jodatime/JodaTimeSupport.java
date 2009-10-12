/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jodatime;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Partial;

import com.mysema.query.annotations.QueryEntity;

/**
 * @author tiwe
 * 
 */
@QueryEntity
public class JodaTimeSupport {

    DateMidnight dateMidnight;

    DateTime dateTime;

    Instant instant;

    LocalDate localDate;

    LocalDateTime localDateTime;

    LocalTime localTime;

    Partial partial;

}
