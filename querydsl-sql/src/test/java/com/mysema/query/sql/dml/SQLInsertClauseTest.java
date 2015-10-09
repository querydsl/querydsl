package com.mysema.query.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.domain.QEmployee;

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
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.populate(emp1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("The order of columns in generated sql should be predictable",
                "insert into EMPLOYEE (ID, FIRSTNAME, LASTNAME, SALARY, DATEFIELD, TIMEFIELD, SUPERIOR_ID)\n" +
                "values (EMPLOYEE.ID, EMPLOYEE.FIRSTNAME, EMPLOYEE.LASTNAME, EMPLOYEE.SALARY, EMPLOYEE.DATEFIELD, EMPLOYEE.TIMEFIELD, EMPLOYEE.SUPERIOR_ID)", sql.getSQL());
    }

}
