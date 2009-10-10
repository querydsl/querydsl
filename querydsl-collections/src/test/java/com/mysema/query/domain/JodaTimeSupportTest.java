package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PTime;

public class JodaTimeSupportTest {

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
}
