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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import java.util.Date;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SCalendarHolidays is a Querydsl query type for SCalendarHolidays
 */
public class SCalendarHolidays extends RelationalPathBase<SCalendarHolidays> {

    private static final long serialVersionUID = 1051026370;

    public static final SCalendarHolidays calendarHolidays = new SCalendarHolidays("CALENDAR_HOLIDAYS");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final DateTimePath<Date> element = createDateTime("ELEMENT", Date.class);

    public final StringPath holidaysKey = createString("HOLIDAYS_KEY");

    public final PrimaryKey<SCalendarHolidays> sql100819184431240 = createPrimaryKey(calendarId, holidaysKey);

    public final ForeignKey<SCalendar> fk31ce1edca61b9464 = new ForeignKey<SCalendar>(this, calendarId, "ID");

    public SCalendarHolidays(String variable) {
        super(SCalendarHolidays.class, forVariable(variable), null, "CALENDAR_HOLIDAYS");
    }

    public SCalendarHolidays(BeanPath<? extends SCalendarHolidays> entity) {
        super(entity.getType(), entity.getMetadata(), null, "CALENDAR_HOLIDAYS");
    }

    public SCalendarHolidays(PathMetadata<?> metadata) {
        super(SCalendarHolidays.class, metadata, null, "CALENDAR_HOLIDAYS");
    }

}

