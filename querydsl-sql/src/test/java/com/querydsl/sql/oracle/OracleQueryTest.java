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
package com.querydsl.sql.oracle;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.domain.QSurvey;

public class OracleQueryTest {

    private OracleQuery<?> query;

    private QSurvey survey = new QSurvey("survey");

    @Before
    public void setUp() {
        query = new OracleQuery<Void>(null, OracleTemplates.builder().newLineToSingleSpace().build());
        query.from(survey);
        query.orderBy(survey.name.asc());
    }

    @Test
    public void connectByPrior() {
        query.connectByPrior(survey.name.isNull());
        assertEquals("from SURVEY survey connect by prior survey.NAME is null order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void connectByPriorNew() {
        query.connectBy(survey.name.prior().isNull());
        assertEquals("from SURVEY survey connect by prior survey.NAME is null order by survey.NAME asc", toString(query));
    }

    @Test
    public void connectByPriorWithSelect() {
        query.select(survey.name.prior().as("parent_name")).connectBy(survey.name.prior().isNull().and(survey.name2.isNotNull()));
        assertEquals("select prior survey.NAME parent_name from SURVEY survey connect by prior survey.NAME is null and survey.NAME2 is not null order by survey.NAME asc", toString(query));
    }

    @Test
    public void connectBy() {
        query.connectBy(survey.name.isNull());
        assertEquals("from SURVEY survey connect by survey.NAME is null order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void connectByNocyclePrior() {
        query.connectByNocyclePrior(survey.name.isNull());
        assertEquals("from SURVEY survey connect by nocycle prior survey.NAME is null order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void startWith() {
        query.startWith(survey.name.isNull());
        assertEquals("from SURVEY survey start with survey.NAME is null order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void orderSiblingsBy() {
        query.orderSiblingsBy(survey.name);
        assertEquals("from SURVEY survey order siblings by survey.NAME order by survey.NAME asc",
                toString(query));
    }

    @Test
    public void rowNum() {
        query.where(OracleGrammar.rownum.lt(5));
        assertEquals("from SURVEY survey where rownum < ? order by survey.NAME asc", toString(query));
    }

    private String toString(OracleQuery query) {
        return query.toString().replace('\n', ' ');
    }
}
