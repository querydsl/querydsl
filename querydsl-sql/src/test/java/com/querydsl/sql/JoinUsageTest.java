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
package com.querydsl.sql;

import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.sql.domain.QSurvey;

public class JoinUsageTest {
    
    @Test(expected=IllegalStateException.class)
    public void Join_Already_Declared() {
        QSurvey survey = QSurvey.survey;
        SQLSubQuery subQuery = new SQLSubQuery(new DefaultQueryMetadata());
        subQuery.from(survey).fullJoin(survey);
    }

}
