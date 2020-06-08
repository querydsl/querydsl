package com.querydsl.r2dbc;

import com.querydsl.core.types.dsl.Param;
import com.querydsl.r2dbc.domain.QSurvey;
import com.querydsl.sql.SQLBindings;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SQLBindingsTest {

    private QSurvey survey = QSurvey.survey;

    private R2DBCQuery<?> query = new R2DBCQuery<Void>();

    @Test
    public void empty() {
        SQLBindings bindings = query.getSQL();
        assertEquals("\nfrom dual", bindings.getSQL());
        assertTrue(bindings.getBindings().isEmpty());
    }

    @Test
    public void singleArg() {
        query.from(survey).where(survey.name.eq("Bob")).select(survey.id);
        SQLBindings bindings = query.getSQL();
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob"), bindings.getBindings());
    }

    @Test
    public void twoArgs() {
        query.from(survey).where(survey.name.eq("Bob"), survey.name2.eq("A")).select(survey.id);
        SQLBindings bindings = query.getSQL();
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ? and SURVEY.NAME2 = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob", "A"), bindings.getBindings());
    }

    @Test
    public void params() {
        Param<String> name = new Param<String>(String.class, "name");
        query.from(survey).where(survey.name.eq(name), survey.name2.eq("A")).select(survey.id);
        query.set(name, "Bob");
        SQLBindings bindings = query.getSQL();
        assertEquals("select SURVEY.ID\nfrom SURVEY SURVEY\nwhere SURVEY.NAME = ? and SURVEY.NAME2 = ?", bindings.getSQL());
        assertEquals(Arrays.asList("Bob", "A"), bindings.getBindings());
    }
}
