package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import org.easymock.EasyMock;
import org.junit.Test;

public class JSR310OffsetTimeTypeTest extends AbstractJSR310DateTimeTypeTest<OffsetTime> {

    public JSR310OffsetTimeTypeTest() {
        super(new JSR310OffsetTimeType());
    }

    @Test
    public void set() throws SQLException {
        OffsetTime value = OffsetTime.now();
        OffsetTime normalized = value.withOffsetSameInstant(ZoneOffset.UTC);
        Time time = new Time(normalized.get(ChronoField.MILLI_OF_DAY));

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTime(1, time, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getTime(1, UTC)).andReturn(new Time(UTC.getTimeInMillis()));
        EasyMock.replay(resultSet);

        OffsetTime result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.getSecond() == 0);
    }
}
