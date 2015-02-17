package com.querydsl.sql.types;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;

public class JSR310LocalTimeTypeTest {
    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private JSR310LocalTimeType type = new JSR310LocalTimeType();

    @BeforeClass
    public static void setUpClass() {
        UTC.setTimeInMillis(0);
    }

    @Test
    public void Set() throws SQLException {
        LocalTime value = LocalTime.now();
        Time time = Time.valueOf(value);

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTime(0, time, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 0, value);
        EasyMock.verify(stmt);
    }
}