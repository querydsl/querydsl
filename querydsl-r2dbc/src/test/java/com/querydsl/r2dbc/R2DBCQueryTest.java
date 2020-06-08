package com.querydsl.r2dbc;

import com.querydsl.r2dbc.domain.QSurvey;
import com.querydsl.sql.SQLExpressions;
import org.junit.Test;

public class R2DBCQueryTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QSurvey survey = QSurvey.survey;
        SQLExpressions.select(survey.id).from(survey).fetch();
    }

}
