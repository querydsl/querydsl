package com.querydsl.r2dbc.types;

import org.junit.Test;

import java.time.LocalDate;

public class JSR310LocalDateTypeTest extends AbstractJSR310DateTimeTypeTest<LocalDate> {

    public JSR310LocalDateTypeTest() {
        super(new JSR310LocalDateType());
    }

    @Test
    public void set() {
//        LocalDate value = LocalDate.now();
//        Date date = new Date(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
//
//        Statement stmt = EasyMock.createNiceMock(Statement.class);
//        stmt.bind(1, date);
//        EasyMock.replay(stmt);
//
//        type.setValue(stmt, 1, value);
//        EasyMock.verify(stmt);
    }

    @Override
    public void get() {
//        Result resultSet = EasyMock.createNiceMock(Result.class);
//        EasyMock.expect(resultSet.(1, UTC)).andReturn(new Date(UTC.getTimeInMillis()));
//        EasyMock.replay(resultSet);
//
//        LocalDate result = type.getValue(resultSet, 1);
//        EasyMock.verify(resultSet);
//
//        assertNotNull(result);
//        assertTrue(result.toEpochDay() == 0);
    }

}
