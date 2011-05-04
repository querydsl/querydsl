package com.mysema.query.sql.mssql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.SQLServerTemplates;
import com.mysema.query.sql.domain.QSurvey;

public class SQLServerQueryTest {

    @Test
    public void TableHints_Single() {
        QSurvey survey = QSurvey.survey;
        SQLServerQuery query = new SQLServerQuery(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT) \nwhere SURVEY.NAME is null", query.toString());
    }
    
    @Test
    public void TableHints_Multiple() {
        QSurvey survey = QSurvey.survey;
        SQLServerQuery query = new SQLServerQuery(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT, SQLServerTableHints.NOLOCK).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT, NOLOCK) \nwhere SURVEY.NAME is null", query.toString());
    }

}
