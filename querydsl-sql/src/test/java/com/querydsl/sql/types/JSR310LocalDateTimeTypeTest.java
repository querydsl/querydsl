package com.querydsl.sql.types;


import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

public class JSR310LocalDateTimeTypeTest {

    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private JSR310LocalDateTimeType type = new JSR310LocalDateTimeType();

    @BeforeClass
    public static void setUpClass() {
        UTC.setTimeInMillis(0);
    }

    @Test
    public void Set() throws SQLException {
        LocalDateTime value = LocalDateTime.now();
        Timestamp ts = new Timestamp(value.toInstant(ZoneOffset.UTC).toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(0, ts, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 0, value);
        EasyMock.verify(stmt);
    }
}