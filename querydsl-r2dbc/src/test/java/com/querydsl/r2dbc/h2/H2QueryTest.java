package com.querydsl.r2dbc.h2;

import com.querydsl.r2dbc.H2Templates;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.r2dbc.domain.QSurvey;
import org.junit.Before;
import org.junit.Test;

public class H2QueryTest {

    private R2DBCQuery<?> query;

    private QSurvey survey = new QSurvey("survey");

    @Before
    public void setUp() {
        query = new R2DBCQuery(H2Templates.builder().newLineToSingleSpace().build());
    }

    @Test
    public void syntax() {
//        SELECT TOP? [DISTINCT | All]? selectExpression
//        FROM tableExpression+
        query.from(survey);
//        WHERE expression+
        query.where(survey.name.isNotNull());
//        GROUP BY expression+
        query.groupBy(survey.name);
//        HAVING expression
        query.having(survey.name.lt(""));
//        [
//          UNION ALL?  select ORDER BY order
//          MINUS
//          EXCEPT
//          INTERSECT
//        ]
//        LIMIT expression
        query.limit(2);
//        OFFSET expression
        query.offset(3);
//        SAMPLE_SIZE rowCountInt
        // TODO
//        FOR UPDATE
        query.forUpdate();
    }

}
