package com.querydsl.sql.postgresql;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;

public class PostgreSQLQueryTest {

    private PostgreSQLQuery<?> query;

    private QSurvey survey = new QSurvey("survey");

    private QEmployee employee = new QEmployee("employee");

    @Before
    public void setUp() {
        query = new PostgreSQLQuery<Void>(null, PostgreSQLTemplates.builder().newLineToSingleSpace().build());
    }

    @Test
    public void syntax() {
//        [ WITH [ RECURSIVE ] with_query [, ...] ]
//        SELECT [ ALL | DISTINCT [ ON ( expression [, ...] ) ] ]
        query.distinctOn(survey.name);
//            * | expression [ [ AS ] output_name ] [, ...]
//            [ FROM from_item [, ...] ]
        query.from(survey);
//            [ WHERE condition ]
        query.where(survey.name.isNull());
//            [ GROUP BY expression [, ...] ]
        query.groupBy(survey.name);
//            [ HAVING condition [, ...] ]
        query.having(survey.id.isNotNull());
//            [ WINDOW window_name AS ( window_definition ) [, ...] ]
        // TODO
//            [ { UNION | INTERSECT | EXCEPT } [ ALL ] select ]
        // TODO INTERSECT
        // TODO EXCEPT
//            [ ORDER BY expression [ ASC | DESC | USING operator ] [ NULLS { FIRST | LAST } ] [, ...] ]
        query.orderBy(survey.name.asc());
//            [ LIMIT { count | ALL } ]
        query.limit(4);
//            [ OFFSET start [ ROW | ROWS ] ]
        query.offset(4);
//            [ FETCH { FIRST | NEXT } [ count ] { ROW | ROWS } ONLY ]
//            [ FOR { UPDATE | SHARE } [ OF table_name [, ...] ] [ NOWAIT ] [...] ]
        query.forUpdate();
        query.forShare();
        query.noWait();

        query.forUpdate().of(survey);

//        where from_item can be one of:
//
//            [ ONLY ] table_name [ * ] [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
//            ( select ) [ AS ] alias [ ( column_alias [, ...] ) ]
//            with_query_name [ [ AS ] alias [ ( column_alias [, ...] ) ] ]
//            function_name ( [ argument [, ...] ] ) [ AS ] alias [ ( column_alias [, ...] | column_definition [, ...] ) ]
//            function_name ( [ argument [, ...] ] ) AS ( column_definition [, ...] )
//            from_item [ NATURAL ] join_type from_item [ ON join_condition | USING ( join_column [, ...] ) ]
//
//        and with_query is:
//
//            with_query_name [ ( column_name [, ...] ) ] AS ( select )
//
//        TABLE { [ ONLY ] table_name [ * ] | with_query_name }
    }

    @Test
    public void forShare() {
        query.from(survey).forShare();
        assertEquals("from SURVEY survey for share", toString(query));
    }

    @Test
    public void forUpDate_noWait() {
        query.from(survey).forUpdate().noWait();
        assertEquals("from SURVEY survey for update nowait", toString(query));
    }

    @Test
    public void forUpdate_of() {
        query.from(survey).forUpdate().of(survey);
        assertEquals("from SURVEY survey for update of SURVEY", toString(query));
    }

    @Test
    public void distinct_on() {
        query.from(employee)
            .distinctOn(employee.datefield, employee.timefield)
            .orderBy(employee.datefield.asc(), employee.timefield.asc(), employee.salary.asc())
            .select(employee.id);

        assertEquals(
            "select distinct on(employee.DATEFIELD, employee.TIMEFIELD) employee.ID from EMPLOYEE employee order by employee.DATEFIELD asc, employee.TIMEFIELD asc, employee.SALARY asc",
            toString(query));
    }

    private String toString(PostgreSQLQuery query) {
        return query.toString().replace('\n', ' ');
    }

}
