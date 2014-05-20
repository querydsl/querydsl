package com.mysema.query.sql.dml;

import com.google.common.collect.ImmutableList;
import com.mysema.query.sql.KeyAccessorsTest.QEmployee;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SQLUpdateClauseTest {

    @Test
    public void GetSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, 1);

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\nset ID = ?", sql.getSQL());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

    @Test
    public void Intertable() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, 1)
              .where(emp1.id.eq(new SQLSubQuery().from(emp2)
                      .where(emp2.superiorId.isNotNull())
                      .unique(emp2.id)));

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\n" +
                "set ID = ?\n" +
                "where EMPLOYEE.ID = (select emp2.ID\n" +
                "from EMPLOYEE emp2\n" +
                "where emp2.SUPERIOR_ID is not null)", sql.getSQL());
    }

    @Test
    public void Intertable2() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLUpdateClause update = new SQLUpdateClause(null, SQLTemplates.DEFAULT, emp1);
        update.set(emp1.id, new SQLSubQuery().from(emp2)
              .where(emp2.superiorId.isNotNull())
              .unique(emp2.id));

        SQLBindings sql = update.getSQL().get(0);
        assertEquals("update EMPLOYEE\n" +
                "set ID = (select emp2.ID\n" +
                "from EMPLOYEE emp2\n" +
                "where emp2.SUPERIOR_ID is not null)", sql.getSQL());
    }

}
