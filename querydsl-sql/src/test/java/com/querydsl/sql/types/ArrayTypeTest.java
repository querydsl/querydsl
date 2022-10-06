package com.querydsl.sql.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Test;

public class ArrayTypeTest {

    @Test
    public void set_object_array() throws SQLException {
        Integer[] value = new Integer[] {1, 2, 3};
        ArrayType<Integer[]> type = new ArrayType<>(Integer[].class, "INTEGER ARRAY");
        
        Array arr = EasyMock.createMock(Array.class);
        Connection conn = EasyMock.createMock(Connection.class);
        PreparedStatement stmt = EasyMock.createMock(PreparedStatement.class);
        EasyMock.expect(stmt.getConnection()).andStubReturn(conn);
        EasyMock.expect(conn.createArrayOf("INTEGER ARRAY", value)).andReturn(arr);
        stmt.setArray(1, arr);
        EasyMock.replay(arr, conn, stmt);
        
        type.setValue(stmt, 1, value);
        
        EasyMock.verify(arr, conn, stmt);
    }
    
    @Test
    public void set_primitive_array() throws SQLException {
        int[] value = new int[] {1, 2, 3};
        Integer[] boxedValue = new Integer[] {1, 2, 3};
        ArrayType<int[]> type = new ArrayType<>(int[].class, "INTEGER ARRAY");
        
        Array arr = EasyMock.createMock(Array.class);
        Connection conn = EasyMock.createMock(Connection.class);
        PreparedStatement stmt = EasyMock.createMock(PreparedStatement.class);
        EasyMock.expect(stmt.getConnection()).andStubReturn(conn);
        EasyMock.expect(conn.createArrayOf("INTEGER ARRAY", boxedValue)).andReturn(arr);
        stmt.setArray(1, arr);
        EasyMock.replay(arr, conn, stmt);
        
        type.setValue(stmt, 1, value);
        
        EasyMock.verify(arr, conn, stmt);
    }
    
    @Test
    public void get_typed_object_array() throws SQLException {
        Integer[] value = new Integer[] {1, 2, 3};
        ArrayType<Integer[]> type = new ArrayType<>(Integer[].class, "INTEGER ARRAY");
        
        ResultSet rs = EasyMock.createMock(ResultSet.class);
        Array arr = EasyMock.createMock(Array.class);
        EasyMock.expect(rs.getArray(1)).andReturn(arr);
        EasyMock.expect(arr.getArray()).andReturn(value);
        EasyMock.replay(rs, arr);
        
        Integer[] result = type.getValue(rs, 1);
        assertSame(value, result);
        
        EasyMock.verify(rs, arr);
    }
    
    @Test
    public void get_generic_object_array() throws SQLException {
        Object[] value = new Object[] {1, 2, 3};
        ArrayType<Integer[]> type = new ArrayType<>(Integer[].class, "INTEGER ARRAY");
        
        ResultSet rs = EasyMock.createMock(ResultSet.class);
        Array arr = EasyMock.createMock(Array.class);
        EasyMock.expect(rs.getArray(1)).andReturn(arr);
        EasyMock.expect(arr.getArray()).andReturn(value);
        EasyMock.replay(rs, arr);
        
        Integer[] result = type.getValue(rs, 1);
        assertArrayEquals(value, result);
        
        EasyMock.verify(rs, arr);
    }
    
    @Test
    public void get_primitive_array() throws SQLException {
        int[] value = new int[] {1, 2, 3};
        Integer[] boxedValue = new Integer[] {1, 2, 3};
        ArrayType<Integer[]> type = new ArrayType<>(Integer[].class, "INTEGER ARRAY");
        
        ResultSet rs = EasyMock.createMock(ResultSet.class);
        Array arr = EasyMock.createMock(Array.class);
        EasyMock.expect(rs.getArray(1)).andReturn(arr);
        EasyMock.expect(arr.getArray()).andReturn(value);
        EasyMock.replay(rs, arr);
        
        Integer[] result = type.getValue(rs, 1);
        assertArrayEquals(boxedValue, result);
        
        EasyMock.verify(rs, arr);
    }

}
