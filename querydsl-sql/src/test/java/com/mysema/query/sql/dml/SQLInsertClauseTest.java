package com.mysema.query.sql.dml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.mysema.query.sql.KeyAccessorsTest.QEmployee;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLTemplates;

public class SQLInsertClauseTest {

    @Test
    public void GetSQL() {
        QEmployee emp1 = new QEmployee("emp1");
        SQLInsertClause insert = new SQLInsertClause(null, SQLTemplates.DEFAULT, emp1);
        insert.set(emp1.id, 1);

        SQLBindings sql = insert.getSQL().get(0);
        assertEquals("insert into EMPLOYEE (SUPERIOR_ID)\nvalues (?)", sql.getSql());
        assertEquals(ImmutableList.of(1), sql.getBindings());
    }

}
