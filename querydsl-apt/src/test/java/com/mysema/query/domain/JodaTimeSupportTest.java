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
package com.mysema.query.domain;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Partial;
import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.TimePath;

public class JodaTimeSupportTest extends AbstractTest {

    @QueryEntity
    public static class JodaTimeSupport {

        DateMidnight dateMidnight;

        DateTime dateTime;

        Instant instant;

        LocalDate localDate;

        LocalDateTime localDateTime;

        LocalTime localTime;

        Partial partial;

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException {
        start(QJodaTimeSupportTest_JodaTimeSupport.class, QJodaTimeSupportTest_JodaTimeSupport.jodaTimeSupport);
        match(DateTimePath.class, "dateMidnight");
        match(DateTimePath.class, "dateTime");
        match(DateTimePath.class, "instant");
        match(DatePath.class, "localDate");
        match(DateTimePath.class, "localDateTime");
        match(TimePath.class, "localTime");
        match(ComparablePath.class, "partial");
    }
}
