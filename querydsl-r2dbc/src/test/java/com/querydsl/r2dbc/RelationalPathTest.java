package com.querydsl.r2dbc;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QTuple;
import com.querydsl.r2dbc.domain.QSurvey;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.querydsl.core.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

public class RelationalPathTest {

    @Test
    public void path() throws ClassNotFoundException, IOException {
        QSurvey survey = QSurvey.survey;
        QSurvey survey2 = serialize(survey);
        assertEquals(Arrays.asList(survey.all()), Arrays.asList(survey2.all()));
        assertEquals(survey.getMetadata(), survey2.getMetadata());
        assertEquals(survey.getMetadata(survey.id), survey2.getMetadata(survey.id));
    }

    @Test
    public void in_tuple() throws ClassNotFoundException, IOException {
        //(survey.id, survey.name)
        QSurvey survey = QSurvey.survey;
        QTuple tuple = Projections.tuple(survey.id, survey.name);
        serialize(tuple);
        serialize(tuple.newInstance(1, "a"));
    }

}
