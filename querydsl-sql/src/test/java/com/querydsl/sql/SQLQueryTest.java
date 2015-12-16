package com.querydsl.sql;

import org.junit.Test;

import com.querydsl.sql.domain.QSurvey;

public class SQLQueryTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QSurvey survey = QSurvey.survey;
        SQLExpressions.select(survey.id).from(survey).fetch();
    }

}
