/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.collections;

import com.querydsl.core.types.Ops;


/**
 * Custom templates which support the Joda Time API instead of the JDK Date API
 *
 * @author tiwe
 *
 */
public class JodaTimeTemplates extends CollQueryTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final JodaTimeTemplates DEFAULT = new JodaTimeTemplates();

    protected JodaTimeTemplates() {
        add(Ops.DateTimeOps.YEAR,         "{0}.getYear()");
        add(Ops.DateTimeOps.MONTH,        "{0}.getMonthOfYear()");
        add(Ops.DateTimeOps.WEEK,         "{0}.getWeekOfWeekyear()");
        add(Ops.DateTimeOps.DAY_OF_WEEK,  "{0}.getDayOfWeek()");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "{0}.getDayOfMonth()");
        add(Ops.DateTimeOps.DAY_OF_YEAR,  "{0}.getDayOfYear()");
        add(Ops.DateTimeOps.HOUR,         "{0}.getHourOfDay()");
        add(Ops.DateTimeOps.MINUTE,       "{0}.getMinuteOfHour()");
        add(Ops.DateTimeOps.SECOND,       "{0}.getSecondOfMinute()");
        add(Ops.DateTimeOps.MILLISECOND,  "{0}.getMillisOfSecond()");

        add(Ops.DateTimeOps.YEAR_MONTH,   "({0}.getYear() * 100 + {0}.getMonthOfYear())");
        add(Ops.DateTimeOps.YEAR_WEEK,    "({0}.getWeekyear() * 100 + {0}.getWeekOfWeekyear())");
    }

}
