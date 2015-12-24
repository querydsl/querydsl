package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

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
        assertEquals(ImmutableList.of(1), sql.getBindings());
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

}
