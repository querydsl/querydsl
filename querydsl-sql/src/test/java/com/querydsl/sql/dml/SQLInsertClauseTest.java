package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.QueryFlag;
import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

import java.util.Collections;

public class SQLInsertClauseTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);
        insert.execute();
    }

    @Test
    public void getSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("insert into EMPLOYEE (ID)\nvalues (?)", sql.getSQL());
        assertEquals(Collections.singletonList(1), sql.getNullFriendlyBindings());
    }

    @Test
    public void bulk() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);
        insert.addBatch();
        insert.set(emp1.id, 2);
        insert.addBatch();
        insert.addFlag(QueryFlag.Position.END, " on duplicate key ignore");
        insert.setBatchToBulk(true);
        assertEquals("insert into EMPLOYEE (ID)\n" +
                "values (?), (?) on duplicate key ignore", insert.getSQL().get(0).getSQL());

    }

    @Test
    public void getSQLWithPreservedColumnOrder() {
        com.querydsl.sql.domain.QEmployee emp1 = new com.querydsl.sql.domain.QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.populate(emp1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("The order of columns in generated sql should be predictable",
                "insert into EMPLOYEE (ID, FIRSTNAME, LASTNAME, SALARY, DATEFIELD, TIMEFIELD, SUPERIOR_ID)\n" +
                "values (EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID)", sql.getSQL());
    }

    @Test
    public void clear() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);
        insert.addBatch();
        assertEquals(1, insert.getBatchCount());
        insert.clear();
        assertEquals(0, insert.getBatchCount());
    }

}
