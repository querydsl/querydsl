package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Types;

import org.junit.Test;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.domain.QEmployee;

public class ColumnMetadataTest {

    @Test
    public void defaultColumn() {
        ColumnMetadata column = ColumnMetadata.named("Person");
        assertEquals("Person", column.getName());
        assertFalse(column.hasJdbcType());
        assertFalse(column.hasLength());
        assertFalse(column.hasPrecisionAndScale());
        assertTrue(column.isInsertable());
        assertTrue(column.isUpdateable());
        assertTrue(column.isNullable());
    }

    @Test
    public void testFullyConfigured() {
        ColumnMetadata column = ColumnMetadata.named("Person").withlength(10).nonInsertable()
                .nonUpdateable().notNull().ofType(Types.BIGINT);
        assertEquals("Person", column.getName());
        assertTrue(column.hasJdbcType());
        assertEquals(Types.BIGINT, column.getJdbcType());
        assertTrue(column.hasLength());
        assertEquals(10, column.getLength());
        assertFalse(column.hasPrecisionAndScale());
        assertFalse(column.isInsertable());
        assertFalse(column.isUpdateable());
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