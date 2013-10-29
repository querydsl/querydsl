package com.mysema.query.sql.dml;

import org.junit.Test;

import com.mysema.query.sql.KeyAccessorsTest.QEmployee;
import com.mysema.query.sql.SQLTemplates;

public class SQLDeleteClauseTest {

    @Test(expected=IllegalArgumentException.class)
    public void Error() {
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLDeleteClause delete = new SQLDeleteClause(null, SQLTemplates.DEFAULT, emp1);
        delete.where(emp2.id.eq(1));
    }

}
