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

import java.util.Calendar;

import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;
import org.joda.time.LocalDate;

public final class Constants {

    private Constants() {}

    public static final java.sql.Date date;

    public static final java.sql.Time time;

    public static final QEmployee employee = new QEmployee("e");

    public static final QEmployee employee2 = new QEmployee("e2");

    public static final QSurvey survey = new QSurvey("s");

    public static final QSurvey survey2 = new QSurvey("s2");

    static{
        LocalDate localDate = new LocalDate(2000, 2, 10);
        date = new java.sql.Date(localDate.toDateMidnight().getMillis());

        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 3, 4);
        cal.set(Calendar.SECOND, 30);
        cal.set(Calendar.MILLISECOND, 0);
        time = new java.sql.Time(cal.getTimeInMillis());
    }
}
