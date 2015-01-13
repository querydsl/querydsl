package com.querydsl.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Types;

import org.junit.Test;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.domain.QEmployee;

public class ColumnMetadataTest {

    @Test
    public void DefaultColumn() {
        ColumnMetadata column = ColumnMetadata.named("Person");
        assertEquals("Person", column.getName());
        assertFalse(column.hasJdbcType());
        assertFalse(column.hasSize());
        assertTrue(column.isNullable());
    }

    @Test
    public void FullyConfigured() {
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
    public void ExtractFromRelationalPath() {
        ColumnMetadata column = ColumnMetadata.getColumnMetadata(QEmployee.employee.id);
        assertEquals("ID", column.getName());
    }

    @Test
    public void FallBackToDefaultWhenMissing() {
        ColumnMetadata column = ColumnMetadata.getColumnMetadata(QEmployee.employee.salary);
        assertEquals("SALARY", column.getName());
    }
}