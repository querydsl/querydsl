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
package com.querydsl.sql.types;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.sql.*;

/**
 * LocalDateType maps LocalDate to Date on the JDBC level
 *
 * @author tiwe
 *
 */
public class LocalDateType extends AbstractDateTimeType<LocalDate> {

    public LocalDateType() {
        super(Types.DATE);
    }

    public LocalDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDate value) {
        return dateFormatter.print(value);
    }

    @Override
    public Class<LocalDate> getReturnedClass() {
        return LocalDate.class;
    }

    @Override
    public LocalDate getValue(ResultSet rs, int startIndex) throws SQLException {
        Date date = rs.getDate(startIndex, utc());
        return date != null ? new LocalDate(date.getTime(), DateTimeZone.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, LocalDate value) throws SQLException {
        st.setDate(startIndex, new Date(value.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis()), utc());
    }

}
