package com.querydsl.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.KeyAccessorsTest.QEmployee;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;

public class SQLInsertClauseTest {

    @Test
    public void GetSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("insert into EMPLOYEE (ID)\nvalues (?)", sql.getSQL());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

    @Test
    public void GetSQLWithPreservedColumnOrder() {
        com.querydsl.sql.domain.QEmployee emp1 = new com.querydsl.sql.domain.QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.populate(emp1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("The order of columns in generated sql should be predictable",
                "insert into EMPLOYEE (SALARY, SUPERIOR_ID, DATEFIELD, FIRSTNAME, TIMEFIELD, ID, LASTNAME)\n" +
                        "values (EMPLOYEE.SALARY, EMPLOYEE.SUPERIOR_ID, EMPLOYEE.DATEFIELD, EMPLOYEE.FIRSTNAME, " +
                        "EMPLOYEE.TIMEFIELD, EMPLOYEE.ID, EMPLOYEE.LASTNAME)", sql.getSQL());
    }

}
