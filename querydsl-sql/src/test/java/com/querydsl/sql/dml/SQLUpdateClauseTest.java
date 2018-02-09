package com.querydsl.sql.dml;

import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

public class SQLUpdateClauseTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, 1);
        update.execute();
    }

    @Test
    public void getSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, 1);

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\nset ID = ?", sql.getSQL());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

    @Test
    public void intertable() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
//CHECKSTYLERULE:OFF: Indentation
        update.set(emp1.id, 1)
                .where(emp1.id.eq(select(emp2.id).from(emp2)
                        .where(emp2.superiorId.isNotNull())));
//CHECKSTYLERULE:ON: Indentation

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\n" +
                "set ID = ?\n" +
                "where EMPLOYEE.ID = (select emp2.ID\n" +
                "from EMPLOYEE emp2\n" +
                "where emp2.SUPERIOR_ID is not null)", sql.getSQL());
    }

    @Test
    public void intertable2() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, select(emp2.id).from(emp2)
                .where(emp2.superiorId.isNotNull()));

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\n" +
                "set ID = (select emp2.ID\n" +
                "from EMPLOYEE emp2\n" +
                "where emp2.SUPERIOR_ID is not null)", sql.getSQL());
    }

    @Test
    public void intertable3() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.superiorId, select(emp2.id).from(emp2)
                .where(emp2.id.eq(emp1.id)));

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\n" +
                "set SUPERIOR_ID = (select emp2.ID\n" +
                "from EMPLOYEE emp2\n" +
                "where emp2.ID = EMPLOYEE.ID)", sql.getSQL());
    }

    @Test
    public void clear() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, 1);
        update.addBatch();
        assertEquals(1, update.getBatchCount());
        update.clear();
        assertEquals(0, update.getBatchCount());
    }

}
