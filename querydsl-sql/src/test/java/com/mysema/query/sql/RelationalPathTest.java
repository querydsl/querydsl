package com.mysema.query.sql;

import static com.mysema.testutil.Serialization.serialize;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.QTuple;

public class RelationalPathTest {

    @Test
    public void Path() throws ClassNotFoundException, IOException {
        QSurvey survey = QSurvey.survey;
        QSurvey survey2 = (QSurvey) serialize(survey);
        assertEquals(Arrays.asList(survey.all()), Arrays.asList(survey2.all()));
        assertEquals(survey.getMetadata(), survey2.getMetadata());
        assertEquals(survey.getMetadata(survey.id), survey2.getMetadata(survey.id));
    }

    @Test
    public void In_Tuple() throws ClassNotFoundException, IOException {
        //(survey.id, survey.name)
        QSurvey survey = QSurvey.survey;
        QTuple tuple = new QTuple(survey.id, survey.name);
        serialize(tuple);
        serialize(tuple.newInstance(1, "a"));
    }

}
