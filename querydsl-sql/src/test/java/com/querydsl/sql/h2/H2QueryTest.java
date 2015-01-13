package com.querydsl.sql.h2;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.domain.QSurvey;

public class H2QueryTest {
    
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
