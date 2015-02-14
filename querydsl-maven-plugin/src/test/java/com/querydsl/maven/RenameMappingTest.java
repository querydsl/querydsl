package com.querydsl.maven;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SchemaAndTable;

public class RenameMappingTest {

    private RenameMapping mapping = new RenameMapping();
    private Configuration configuration = new Configuration(SQLTemplates.DEFAULT);

    // to schema

    @Test
    public void SchemaToSchema() {
        mapping.fromSchema = "ABC";
        mapping.toSchema = "DEF";
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
    public void TableToTable() {
        mapping.fromTable = "TABLE1";
        mapping.toTable = "TABLE2";
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("DEF", "TABLE2"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE1")));
        assertEquals(
                new SchemaAndTable("DEF", "TABLE3"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE3")));
    }

    @Test
    public void SchemaTableToTable() {
        mapping.fromSchema = "ABC";
        mapping.fromTable = "TABLE1";
        mapping.toTable = "TABLE2";
        mapping.apply(configuration);

        assertEquals(
                new SchemaAndTable("ABC", "TABLE2"),
                configuration.getOverride(new SchemaAndTable("ABC", "TABLE1")));
        assertEquals(
                new SchemaAndTable("DEF", "TABLE1"),
                configuration.getOverride(new SchemaAndTable("DEF", "TABLE1")));
    }

    @Test
    public void SchemaTableToSchemaTable() {
        mapping.fromSchema = "ABC";
        mapping.fromTable = "TABLE1";
        mapping.toSchema = "ABC";
        mapping.toTable = "TABLE2";
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
    public void SchemaTableColumnToColumn() {
        mapping.fromSchema = "ABC";
        mapping.fromTable = "TABLE1";
        mapping.fromColumn = "COLUMN1";
        mapping.toColumn = "COLUMN2";
        mapping.apply(configuration);

        assertEquals(
                "COLUMN2",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE1"), "COLUMN1"));
        assertEquals(
                "COLUMN1",
                configuration.getColumnOverride(new SchemaAndTable("DEF", "TABLE1"), "COLUMN1"));
    }

    @Test
    public void TableColumnToColumn() {
        mapping.fromTable = "TABLE1";
        mapping.fromColumn = "COLUMN1";
        mapping.toColumn = "COLUMN2";
        mapping.apply(configuration);

        assertEquals(
                "COLUMN2",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE1"), "COLUMN1"));
        assertEquals(
                "COLUMN1",
                configuration.getColumnOverride(new SchemaAndTable("ABC", "TABLE2"), "COLUMN1"));
    }
}
