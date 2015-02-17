package com.querydsl.sql.types;


import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;

public class JSR310InstantTypeTest {
    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private JSR310InstantType type = new JSR310InstantType();

    @BeforeClass
    public static void setUpClass() {
        UTC.setTimeInMillis(0);
    }

    @Test
    public void Set() throws SQLException {
        Instant value = Instant.now();
        Timestamp ts = new Timestamp(value.toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(0, ts, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 0, value);
        EasyMock.verify(stmt);
    }
}