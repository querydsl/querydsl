package com.querydsl.r2dbc.types;

import org.junit.Test;

import java.time.OffsetTime;

public class JSR310OffsetTimeTypeTest extends AbstractJSR310DateTimeTypeTest<OffsetTime> {

    public JSR310OffsetTimeTypeTest() {
        super(new JSR310OffsetTimeType());
    }

    @Test
    public void set() {
//        OffsetTime value = OffsetTime.now();
//        OffsetTime normalized = value.withOffsetSameInstant(ZoneOffset.UTC);
//        Time time = new Time(normalized.get(ChronoField.MILLI_OF_DAY));
//
//        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
//        stmt.setTime(1, time, UTC);
//        EasyMock.replay(stmt);
//
//        type.setValue(stmt, 1, value);
//        EasyMock.verify(stmt);
    }

    @Test
    public void get() {
//        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
//        EasyMock.expect(resultSet.getTime(1, UTC)).andReturn(new Time(UTC.getTimeInMillis()));
//        EasyMock.replay(resultSet);
//
//        OffsetTime result = type.getValue(resultSet, 1);
//        EasyMock.verify(resultSet);
//
//        assertNotNull(result);
//        assertTrue(result.getSecond() == 0);
    }
}
