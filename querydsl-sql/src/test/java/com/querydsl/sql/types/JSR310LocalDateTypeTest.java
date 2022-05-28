package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.easymock.EasyMock;
import org.junit.Test;

public class JSR310LocalDateTypeTest extends AbstractJSR310DateTimeTypeTest<LocalDate> {

    public JSR310LocalDateTypeTest() {
        super(new JSR310LocalDateType());
    }

    @Test
    public void set() throws SQLException {
        LocalDate value = LocalDate.now();
        Date date = new Date(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setDate(1, date, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        LocalDate value = LocalDate.now();
        Date date = new Date(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setDate(1, date, UTC);
        EasyMock.replay(stmt);

        new LocalDateType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getDate(1, UTC)).andReturn(new Date(70, 0, 1));
        EasyMock.replay(resultSet);

        LocalDate result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toEpochDay() == 0);
    }
}
