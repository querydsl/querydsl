/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc.mssql;

import com.querydsl.r2dbc.SQLServerTemplates;
import com.querydsl.r2dbc.domain.QEmployee;
import com.querydsl.r2dbc.domain.QSurvey;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SQLServerQueryTest {

    private static final QSurvey survey = QSurvey.survey;

    @Test
    public void tableHints_single() {
        R2DBCSQLServerQuery<?> query = new R2DBCSQLServerQuery<Void>(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT)\nwhere SURVEY.NAME is null", query.toString());
    }

    @Test
    public void tableHints_multiple() {
        R2DBCSQLServerQuery<?> query = new R2DBCSQLServerQuery<Void>(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT, SQLServerTableHints.NOLOCK).where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT, NOLOCK)\nwhere SURVEY.NAME is null", query.toString());
    }

    @Test
    public void tableHints_multiple2() {
        QSurvey survey2 = new QSurvey("survey2");
        R2DBCSQLServerQuery<?> query = new R2DBCSQLServerQuery<Void>(null, new SQLServerTemplates());
        query.from(survey).tableHints(SQLServerTableHints.NOWAIT)
                .from(survey2).tableHints(SQLServerTableHints.NOLOCK)
                .where(survey.name.isNull());
        assertEquals("from SURVEY SURVEY with (NOWAIT), SURVEY survey2 with (NOLOCK)\nwhere SURVEY.NAME is null", query.toString());
    }


    @Test
    public void join_tableHints_single() {
        QEmployee employee1 = QEmployee.employee;
        QEmployee employee2 = new QEmployee("employee2");
        R2DBCSQLServerQuery<?> query = new R2DBCSQLServerQuery<Void>(null, new SQLServerTemplates());
        query.from(employee1).tableHints(SQLServerTableHints.NOLOCK)
                .join(employee2).tableHints(SQLServerTableHints.NOLOCK)
                .on(employee1.superiorId.eq(employee2.id));
        assertEquals("from EMPLOYEE EMPLOYEE with (NOLOCK)\njoin EMPLOYEE employee2 with (NOLOCK)\non EMPLOYEE.SUPERIOR_ID = employee2.ID", query.toString());
    }

    @Test
    public void join_tableHints_multiple() {
        QEmployee employee1 = QEmployee.employee;
        QEmployee employee2 = new QEmployee("employee2");
        R2DBCSQLServerQuery<?> query = new R2DBCSQLServerQuery<Void>(null, new SQLServerTemplates());
        query.from(employee1).tableHints(SQLServerTableHints.NOLOCK, SQLServerTableHints.READUNCOMMITTED)
                .join(employee2).tableHints(SQLServerTableHints.NOLOCK, SQLServerTableHints.READUNCOMMITTED)
                .on(employee1.superiorId.eq(employee2.id));
        assertEquals("from EMPLOYEE EMPLOYEE with (NOLOCK, READUNCOMMITTED)\njoin EMPLOYEE employee2 with (NOLOCK, READUNCOMMITTED)\non EMPLOYEE.SUPERIOR_ID = employee2.ID", query.toString());
    }

}
