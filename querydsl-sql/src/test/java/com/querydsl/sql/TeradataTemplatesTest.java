package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TeradataTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new TeradataTemplates();
    }

    @Test
    public void Limit() {
        query.from(survey1).limit(5);
        assertEquals("from SURVEY survey1 " +
            "qualify row_number() over (order by 1) <= ?", query.toString());
    }

    @Test
    public void Offset() {
        query.from(survey1).offset(5);
        assertEquals("from SURVEY survey1 " +
            "qualify row_number() over (order by 1) > ?", query.toString());
    }

    @Test
    public void Limit_Offset() {
        query.from(survey1).limit(5).offset(10);
        assertEquals("from SURVEY survey1 " +
            "qualify row_number() over (order by 1) between ? and ?", query.toString());
    }

    @Test
    public void OrderBy_Limit() {
        query.from(survey1).orderBy(survey1.name.asc()).limit(5);
        assertEquals("from SURVEY survey1 " +
            "order by survey1.NAME asc " +
            "qualify row_number() over (order by survey1.NAME asc) <= ?", query.toString());
    }

}
