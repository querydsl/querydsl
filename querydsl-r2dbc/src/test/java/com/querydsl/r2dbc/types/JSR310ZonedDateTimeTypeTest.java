package com.querydsl.r2dbc.types;

import org.junit.Test;

import java.time.ZonedDateTime;

public class JSR310ZonedDateTimeTypeTest extends AbstractJSR310DateTimeTypeTest<ZonedDateTime> {

    public JSR310ZonedDateTimeTypeTest() {
        super(new JSR310ZonedDateTimeType());
    }

    @Test
    public void set() {
//        ZonedDateTime value = ZonedDateTime.now();
//        Timestamp ts = new Timestamp(value.toInstant().toEpochMilli());
//
//        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
//        stmt.setTimestamp(1, ts, UTC);
//        EasyMock.replay(stmt);
//
//        type.setValue(stmt, 1, value);
//        EasyMock.verify(stmt);
    }

    @Test
    public void get() {
//        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
//        EasyMock.expect(resultSet.getTimestamp(1, UTC)).andReturn(new Timestamp(UTC.getTimeInMillis()));
//        EasyMock.replay(resultSet);
//
//        ZonedDateTime result = type.getValue(resultSet, 1);
//        EasyMock.verify(resultSet);
//
//        assertNotNull(result);
//        assertTrue(result.toEpochSecond() == 0);
    }
}
