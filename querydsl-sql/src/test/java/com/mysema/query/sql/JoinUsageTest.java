package com.mysema.query.sql;

import org.junit.Test;

import com.mysema.query.sql.domain.QSurvey;

public class JoinUsageTest {
    
    @Test(expected=IllegalStateException.class)
    public void Join_Already_Declared(){
        SQLSubQuery subQuery = new SQLSubQuery();
        subQuery.from(QSurvey.survey).fullJoin(QSurvey.survey);
    }

}
