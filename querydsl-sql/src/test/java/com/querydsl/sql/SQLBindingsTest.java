package com.querydsl.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.types.expr.Param;

public class SQLBindingsTest {

    private QSurvey survey = QSurvey.survey;

    private SQLQuery query = new SQLQuery(SQLTemplates.DEFAULT);

    @Test
    public void Empty() {
        SQLBindings bindings = query.getSQL();
        assertEquals("\nfrom dual", bindings.getSQL());
        assertTrue(bindings.getBindings().isEmpty());
    }

    @Test
    public void SingleArg() {
        query.from(survey).where(survey.name.eq("Bob"));
        SQLBindings bindings = query.getSQL(survey.id);
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob"), bindings.getBindings());
    }

    @Test
    public void TwoArgs() {
        query.from(survey).where(survey.name.eq("Bob"), survey.name2.eq("A"));
        SQLBindings bindings = query.getSQL(survey.id);
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ? and SURVEY.NAME2 = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob", "A"), bindings.getBindings());
    }

    @Test
    public void Params() {
        Param<String> name = new Param<String>(String.class, "name");
        query.from(survey).where(survey.name.eq(name), survey.name2.eq("A"));
        query.set(name, "Bob");
        SQLBindings bindings = query.getSQL(survey.id);
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ? and SURVEY.NAME2 = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob", "A"), bindings.getBindings());
    }
}
