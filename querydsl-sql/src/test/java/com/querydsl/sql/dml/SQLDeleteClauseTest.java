package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

import java.util.Collections;

public class SQLDeleteClauseTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp1.id.eq(1));
        delete.execute();
    }

    @Test(expected = IllegalArgumentException.class)
    @Ignore
    public void error() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp2.id.eq(1));
    }

    @Test
    public void getSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp1.id.eq(1));

        SQLBindings sql = delete.getSQL().get(0);
        assertEquals("delete from EMPLOYEE\nwhere EMPLOYEE.ID = ?", sql.getSQL());
        assertEquals(Collections.singletonList(1), sql.getNullFriendlyBindings());
    }

    @Test
    public void clear() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp1.id.eq(1));
        delete.addBatch();
        assertEquals(1, delete.getBatchCount());
        delete.clear();
        assertEquals(0, delete.getBatchCount());
    }

}
