package com.querydsl.r2dbc.codegen.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.SchemaAndTable;

public class RenameMappingTest {

    private RenameMapping mapping = new RenameMapping();
    private Configuration configuration = new Configuration(SQLTemplates.DEFAULT);

    // to schema

    @Test
    public void schemaToSchema() {
        mapping.setFromSchema("ABC");
        mapping.setToSchema("DEF");
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("DEF", "TABLE"),
                configuration.getOverride(new SchemaAndTable("ABC", "TABLE")));
        assertEquals(
                new SchemaAndTable("ABCD", "TABLE"),
                configuration.getOverride(new SchemaAndTable("ABCD", "TABLE")));

    }

    // to table

    @Test
    public void tableToTable() {
        mapping.setFromTable("TABLE1");
        mapping.setToTable("TABLE2");
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("DEF", "TABLE2"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE1")));
        assertEquals(
                new SchemaAndTable("DEF", "TABLE3"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE3")));
    }

    @Test
    public void schemaTableToTable() {
        mapping.setFromSchema("ABC");
        mapping.setFromTable("TABLE1");
        mapping.setToTable("TABLE2");
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("ABC", "TABLE2"),
                configuration.getOverride(new SchemaAndTable("ABC", "TABLE1")));
        assertEquals(
                new SchemaAndTable("DEF", "TABLE1"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE1")));
    }

    @Test
    public void schemaTableToSchemaTable() {
        mapping.setFromSchema("ABC");
        mapping.setFromTable("TABLE1");
        mapping.setToSchema("ABC");
        mapping.setToTable("TABLE2");
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("ABC", "TABLE2"),
                configuration.getOverride(new SchemaAndTable("ABC", "TABLE1")));
        assertEquals(
                new SchemaAndTable("DEF", "TABLE1"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE1")));
    }

    // to column

    @Test
    public void schemaTableColumnToColumn() {
        mapping.setFromSchema("ABC");
        mapping.setFromTable("TABLE1");
        mapping.setFromColumn("COLUMN1");
        mapping.setToColumn("COLUMN2");
        mapping.apply(configuration);

        assertEquals(
                "COLUMN2",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE1"), "COLUMN1"));
        assertEquals(
                "COLUMN1",
                configuration.getColumnOverride(new SchemaAndTable("DEF", "TABLE1"), "COLUMN1"));
    }

    @Test
    public void tableColumnToColumn() {
        mapping.setFromTable("TABLE1");
        mapping.setFromColumn("COLUMN1");
        mapping.setToColumn("COLUMN2");
        mapping.apply(configuration);

        assertEquals(
                "COLUMN2",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE1"), "COLUMN1"));
        assertEquals(
                "COLUMN1",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE2"), "COLUMN1"));
    }
}
