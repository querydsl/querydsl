package com.querydsl.r2dbc.types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class AbstractJSR310DateTimeTypeTest<T extends Temporal> {

    protected static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected final AbstractJSR310DateTimeType<T> type;

    public AbstractJSR310DateTimeTypeTest(AbstractJSR310DateTimeType<T> type) {
        this.type = type;
    }

    private TimeZone tz;

    @Before
    public void before() {
        tz = TimeZone.getDefault();
    }

    @After
    public void after() {
        TimeZone.setDefault(tz);
    }

    public abstract void set();

    public abstract void get();

    @Test
    public void set_cST() {
        TimeZone.setDefault(TimeZone.getTimeZone("CST")); // -6:00
        set();
    }

    @Test
    public void set_iOT() {
        TimeZone.setDefault(TimeZone.getTimeZone("IOT")); // +6:00
        set();
    }

    @Test
    public void get_cST() {
        TimeZone.setDefault(TimeZone.getTimeZone("CST")); // -6:00
        get();
    }

    @Test
    public void get_iOT() {
        TimeZone.setDefault(TimeZone.getTimeZone("IOT")); // +6:00
        get();
    }

}
