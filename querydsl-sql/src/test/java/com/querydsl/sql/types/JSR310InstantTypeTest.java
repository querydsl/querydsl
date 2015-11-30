package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import org.easymock.EasyMock;
import org.junit.Test;

public class JSR310InstantTypeTest extends AbstractJSR310DateTimeTypeTest<Instant> {

    public JSR310InstantTypeTest() {
        super(new JSR310InstantType());
    }

    @Test
    public void set() throws SQLException {
        Instant value = Instant.now();
        Timestamp ts = new Timestamp(value.toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        Instant value = Instant.now();
        Timestamp ts = new Timestamp(value.toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTimestamp(1, ts);
        EasyMock.replay(stmt);

        new DateTimeType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getTimestamp(1, UTC)).andReturn(new Timestamp(UTC.getTimeInMillis()));
        EasyMock.replay(resultSet);

        Instant result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toEpochMilli() == 0);
    }
}
