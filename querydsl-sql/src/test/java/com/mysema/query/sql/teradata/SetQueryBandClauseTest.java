package com.mysema.query.sql.teradata;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLTemplates;

public class SetQueryBandClauseTest {

    private SetQueryBandClause clause;

    @Before
    public void setUp() {
        Configuration conf = new Configuration(SQLTemplates.DEFAULT);
        clause = new SetQueryBandClause(null, conf);
    }

    @Test
    public void ToString() {
        clause.set("a", "b");
        assertEquals("set query band = 'a=b;' for session", clause.toString());
    }

    @Test
    public void ForTransaction() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query band = 'b=c;a=b;' for transaction", clause.toString());
    }

    @Test
    public void GetSQL() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query band = 'b=c;a=b;' for transaction", clause.getSQL().get(0).getSQL());
    }

}
