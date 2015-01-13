package com.querydsl.sql.teradata;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SetQueryBandClauseTest {

    private Configuration conf;

    private SetQueryBandClause clause;

    @Before
    public void setUp() {
        conf = new Configuration(SQLTemplates.DEFAULT);
        conf.setUseLiterals(true);
        clause = new SetQueryBandClause(null, conf);
    }

    @Test
    public void ToString() {
        clause.set("a", "b");
        assertEquals("set query_band='a=b;' for session", clause.toString());
    }

    @Test
    public void ToString2() {
        conf.setUseLiterals(false);
        clause.set("a", "b");
        clause.forTransaction();
        assertEquals("set query_band=? for transaction", clause.toString());
    }

    @Test
    public void ForTransaction() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query_band='a=b;b=c;' for transaction", clause.toString());
    }

    @Test
    public void GetSQL() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query_band='a=b;b=c;' for transaction", clause.getSQL().get(0).getSQL());
    }

}
