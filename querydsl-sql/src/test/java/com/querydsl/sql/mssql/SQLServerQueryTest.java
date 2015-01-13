/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.mssql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.domain.QSurvey;

public class SQLServerQueryTest {

    private static final QSurvey survey = QSurvey.survey;
    
    @Test
    public void TableHints_Single() {        
        SQLServerQuery query = new SQLServerQuery(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT)\nwhere SURVEY.NAME is null", query.toString());
    }
    
    @Test
    public void TableHints_Multiple() {
        SQLServerQuery query = new SQLServerQuery(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT, SQLServerTableHints.NOLOCK).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT, NOLOCK)\nwhere SURVEY.NAME is null", query.toString());
    }
    
    @Test
    public void TableHints_Multiple2() {
        QSurvey survey2 = new QSurvey("survey2");
        SQLServerQuery query = new SQLServerQuery(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT)
             .from(survey2).tableHints(SQLServerTableHints.NOLOCK)
             .where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT), SURVEY survey2 with (NOLOCK)\nwhere SURVEY.NAME is null", query.toString());
    }

}
