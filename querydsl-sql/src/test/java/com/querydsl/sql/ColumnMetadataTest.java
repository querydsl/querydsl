package com.querydsl.sql;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.Test;

import com.querydsl.sql.domain.QEmployee;

public class ColumnMetadataTest {

    @Test
    public void defaultColumn() {
        ColumnMetadata column = ColumnMetadata.named("Person");
        assertEquals("Person", column.getName());
        assertFalse(column.hasJdbcType());
        assertFalse(column.hasSize());
        assertTrue(column.isNullable());
    }

    @Test
    public void fullyConfigured() {
        ColumnMetadata column = ColumnMetadata.named("Person").withSize(10)
                .notNull().ofType(Types.BIGINT);
        assertEquals("Person", column.getName());
        assertTrue(column.hasJdbcType());
        assertEquals(Types.BIGINT, column.getJdbcType());
        assertTrue(column.hasSize());
        assertEquals(10, column.getSize());
        assertFalse(column.isNullable());
    }

    @Test
    public void extractFromRelationalPath() {
        ColumnMetadata column = ColumnMetadata.getColumnMetadata(QEmployee.employee.id);
        assertEquals("ID", column.getName());
    }

    @Test
    public void fallBackToDefaultWhenMissing() {
        ColumnMetadata column = ColumnMetadata.getColumnMetadata(QEmployee.employee.salary);
        assertEquals("SALARY", column.getName());
    }
}