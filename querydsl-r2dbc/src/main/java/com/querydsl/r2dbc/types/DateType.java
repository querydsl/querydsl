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
package com.querydsl.r2dbc.types;

import java.sql.Date;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.Temporal;

/**
 * {@code DateType} maps Date to Date on the JDBC level
 *
 * @author mc_fish
 */
public class DateType extends AbstractDateTimeType<Date, Temporal> {

    public DateType() {
        super(Types.DATE);
    }

    public DateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Date value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<Date> getReturnedClass() {
        return Date.class;
    }

    @Override
    public Class<Temporal> getDatabaseClass() {
        return Temporal.class;
    }

    @Override
    protected LocalDate toDbValue(Date value) {
        return value.toLocalDate();
    }

    @Override
    protected Date fromDbValue(Temporal value) {
        if (LocalDate.class.isAssignableFrom(value.getClass())) {
            return Date.valueOf((LocalDate) value);
        }

        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            LocalDate localDate = ((LocalDateTime) value).toLocalDate();
            return Date.valueOf(localDate);
        }
        //mysql
//        if (String.class.isAssignableFrom(value.getClass())) {
//            return Date.valueOf(LocalDateTime.parse((String) value, localDateFormatter), ZoneOffset.UTC).toLocalDate());
//        }

        Instant instant = Instant.from(value);
        return Date.valueOf(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).toLocalDate());
    }

}
