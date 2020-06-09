package com.querydsl.r2dbc;

import com.querydsl.r2dbc.domain.QSurvey;
import org.junit.Test;

public class R2DBCQueryTest {

    @Test(expected = IllegalStateException.class)
    public void noConnection() {
        QSurvey survey = QSurvey.survey;
        R2DBCExpressions.select(survey.id).from(survey).fetch().collectList().block();
    }

}
