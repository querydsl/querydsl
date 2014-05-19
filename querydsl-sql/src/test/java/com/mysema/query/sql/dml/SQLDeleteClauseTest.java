package com.mysema.query.sql.dml;

import com.google.common.collect.ImmutableList;
import com.mysema.query.sql.KeyAccessorsTest.QEmployee;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLTemplates;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SQLDeleteClauseTest {

    @Test(expected=IllegalArgumentException.class)
    public void Error() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp2.id.eq(1));
    }

    @Test
    public void GetSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp1.id.eq(1));

        SQLBindings sql = delete.getSQL().get(0);
        assertEquals("delete from EMPLOYEE\nwhere EMPLOYEE.ID = ?", sql.getSQL());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

}
