package com.querydsl.sql.types;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

public class JSR310LocalDateTypeTest {
    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private JSR310LocalDateType type = new JSR310LocalDateType();

    @BeforeClass
    public static void setUpClass() {
        UTC.setTimeInMillis(0);
    }

    @Test
    public void Set() throws SQLException {
        LocalDate value = LocalDate.now();
        Date date = Date.valueOf(value);

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setDate(0, date, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 0, value);
        EasyMock.verify(stmt);
    }
}