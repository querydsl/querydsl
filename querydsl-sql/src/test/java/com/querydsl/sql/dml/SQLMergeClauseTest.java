package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.KeyAccessorsTest;

public class SQLMergeClauseTest {

    @Test
    public void clear() {
        KeyAccessorsTest.QEmployee emp1 = new KeyAccessorsTest.QEmployee("emp1");
        SQLMergeClause merge = new SQLMergeClause(null, new H2Templates(), emp1);
        merge.set(emp1.id, 1);
        merge.addBatch();
        assertEquals(1, merge.getBatchCount());
        merge.clear();
        assertEquals(0, merge.getBatchCount());
    }
}
