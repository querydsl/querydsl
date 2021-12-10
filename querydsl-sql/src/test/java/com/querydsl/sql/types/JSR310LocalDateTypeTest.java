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
import org.easymock.Mock;
import org.junit.Test;

public class JSR310LocalDateTypeTest extends AbstractJSR310DateTimeTypeTest<LocalDate> {

    public JSR310LocalDateTypeTest() {
        super(new JSR310LocalDateType());
    }

    @Mock
    private ResultSet resultSet;

    /**
     * testIncorrectLocalDateTypeBug is to prove that date conversion issue has been resolved
     * @param
     * @return void
     */

    @Test
    public void testIncorrectLocalDateTypeBug() throws SQLException {
        JSR310LocalDateType jSR310LocalDateType = new JSR310LocalDateType();
        resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getString("id")).thenReturn("myId");
        Mockito.when(resultSet.getString("sysname")).thenReturn("myHost");
        Mockito.when(resultSet.getString("zos")).thenReturn("myOS");
        Mockito.when(resultSet.getString("customer_name")).thenReturn("myCustomerName");
        String str="2021-08-10";
        Date date=Date.valueOf(str);//converting string into sql date
        Mockito.when(resultSet.getDate(0, utc())).thenReturn(date);
        LocalDate localDate = jSR310LocalDateType.getValue(resultSet, 0);
        assertEquals( 2021, localDate.getYear());
        assertEquals(Month.AUGUST, localDate.getMonth());
        assertEquals( 10, localDate.getDayOfMonth());
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
        EasyMock.expect(resultSet.getDate(1, UTC)).andReturn(new Date(UTC.getTimeInMillis()));
        EasyMock.replay(resultSet);

        LocalDate result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toEpochDay() == 0);
    }
}
