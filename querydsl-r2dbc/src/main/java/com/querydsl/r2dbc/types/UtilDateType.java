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

import java.sql.Types;
import java.time.*;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * {@code UtilDateType} maps Date to Timestamp on the JDBC level
 *
 * @author mc_fish
 */
public class UtilDateType extends AbstractDateTimeType<Date, Temporal> {

    public UtilDateType() {
        super(Types.TIMESTAMP);
    }

    public UtilDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Date value) {
        return dateTimeFormatter.format(value);
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
    protected LocalDateTime toDbValue(Date value) {
        return LocalDateTime.ofInstant(value.toInstant(), ZoneOffset.systemDefault());
    }

    @Override
    protected Date fromDbValue(Temporal value) {
        if (LocalDate.class.isAssignableFrom(value.getClass())) {
            Instant instant = ((LocalDate) value).atStartOfDay().atZone(ZoneOffset.systemDefault()).toInstant();
            return Date.from(instant);
        }

        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            Instant instant = ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        }

        Instant instant = Instant.from(value);
        return Date.from(instant);
    }

}
