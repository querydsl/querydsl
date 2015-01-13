package com.querydsl.sql.hsqldb;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.domain.QSurvey;

public class HsqldbQueryTest {
    
    private SQLQuery query;
    
    private QSurvey survey = new QSurvey("survey");
    
    @Before
    public void setUp() {
        query = new SQLQuery(null, new H2Templates() {{
            newLineToSingleSpace();
        }});
    }
    
    @Test
    public void Syntax() {
//        SELECT [{LIMIT <offset> <limit> | TOP <limit>}[2]][ALL | DISTINCT]
//        { selectExpression | table.* | * } [, ...]
//        [INTO [CACHED | TEMP  | TEXT][2] newTable]
//        FROM tableList
        query.from(survey);
//        [WHERE Expression]
        query.where(survey.id.isNotNull());
//        [GROUP BY Expression [, ...]]
        query.groupBy(survey.name);
//        [HAVING Expression]
        query.having(survey.id.isNotNull());
//        [{ UNION [ALL | DISTINCT] | {MINUS [DISTINCT] | EXCEPT [DISTINCT] } |
        // TODO MINUS
        // TODO EXCEPT
//        INTERSECT [DISTINCT] } selectStatement]
        // TODO INTERSECT
//        [ORDER BY orderExpression [, ...]]
        query.orderBy(survey.name.asc());
//        [LIMIT <limit> [OFFSET <offset>]];
        query.limit(4);
        query.offset(4);
        
    }

}
