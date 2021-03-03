package com.querydsl.sql.types;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Common abstract superclass for Type implementations for Joda-Time
 *
 * @param <T>
 */
public abstract class AbstractJodaTimeDateTimeType<T> extends AbstractType<T> {

    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected static Calendar utc() {
        return (Calendar) UTC.clone();
    }

    protected static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    protected static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    protected static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

    public AbstractJodaTimeDateTimeType(int type) {
        super(type);
    }
}
