package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.sql.domain.QSurvey;

public class SQLBindingsTest {

    private QSurvey survey = QSurvey.survey;

    private SQLQuery query = new SQLQuery(SQLTemplates.DEFAULT);

    @Test
    public void Empty() {
        SQLBindings bindings = query.getSQL();
        assertEquals("\nfrom dual", bindings.getSql());
        assertTrue(bindings.getBindings().isEmpty());
    }

    @Test
    public void SingleArg() {
        query.from(survey).where(survey.name.eq("Bob"));
        SQLBindings bindings = query.getSQL(survey.id);
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ?", bindings.getSql());
        assertEquals(Arrays.asList("Bob"), bindings.getBindings());
    }

    @Test
    public void TwoArgs() {
        query.from(survey).where(survey.name.eq("Bob"), survey.name2.eq("A"));
        SQLBindings bindings = query.getSQL(survey.id);
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ? and SURVEY.NAME2 = ?", bindings.getSql());
        assertEquals(Arrays.asList("Bob", "A"), bindings.getBindings());
    }
}
