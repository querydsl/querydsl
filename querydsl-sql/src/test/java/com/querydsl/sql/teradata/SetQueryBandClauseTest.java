package com.querydsl.sql.teradata;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

public class SetQueryBandClauseTest {

    private Configuration conf;

    private SetQueryBandClause clause;

    @Before
    public void setUp() {
        conf = new Configuration(SQLTemplates.DEFAULT);
        conf.setUseLiterals(true);
        clause = new SetQueryBandClause((Connection) null, conf);
    }

    @Test
    public void toString_() {
        clause.set("a", "b");
        assertEquals("set query_band='a=b;' for session", clause.toString());
    }

    @Test
    public void toString2() {
        conf.setUseLiterals(false);
        clause.set("a", "b");
        clause.forTransaction();
        assertEquals("set query_band=? for transaction", clause.toString());
    }

    @Test
    public void forTransaction() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query_band='a=b;b=c;' for transaction", clause.toString());
    }

    @Test
    public void getSQL() {
        clause.forTransaction();
        clause.set("a", "b");
        clause.set("b", "c");
        assertEquals("set query_band='a=b;b=c;' for transaction", clause.getSQL().get(0).getSQL());
    }

}
