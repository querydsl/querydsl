package com.querydsl.r2dbc;

import com.querydsl.sql.SchemaAndTable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SchemaAndTableTest {

    @Test
    public void equalsAndHashCode() {
        SchemaAndTable st1 = new SchemaAndTable(null, "table");
        SchemaAndTable st2 = new SchemaAndTable(null, "table");
        assertEquals(st1, st2);
        assertEquals(st1.hashCode(), st2.hashCode());
    }

}
