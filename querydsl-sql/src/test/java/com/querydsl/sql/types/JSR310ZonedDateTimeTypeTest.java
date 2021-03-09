package com.querydsl.sql.types;

import org.easymock.EasyMock;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JSR310ZonedDateTimeTypeTest extends AbstractJSR310DateTimeTypeTest<ZonedDateTime> {

    public JSR310ZonedDateTimeTypeTest() {
        super(new JSR310ZonedDateTimeType());
    }

    @Test
    public void set() throws SQLException {
        ZonedDateTime value = ZonedDateTime.now();
        Timestamp ts = Timestamp.from(value.toInstant());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        ZonedDateTime value = ZonedDateTime.now();
        Timestamp ts = Timestamp.from(value.toInstant());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts);
        EasyMock.replay(stmt);

        new DateTimeType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getTimestamp(1, UTC)).andReturn(Timestamp.from(UTC.toInstant()));
        EasyMock.replay(resultSet);

        ZonedDateTime result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toEpochSecond() == 0);
    }
}
