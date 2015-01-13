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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.sql.*;

/**
 * LocalDateTimeType maps LocalDateTime to Timestamp on the JDBC level
 *
 * @author tiwe
 *
 */
public class LocalDateTimeType extends AbstractDateTimeType<LocalDateTime> {

    public LocalDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public LocalDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDateTime value) {
        return dateTimeFormatter.print(value);
    }

    @Override
    public Class<LocalDateTime> getReturnedClass() {
        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime getValue(ResultSet rs, int index) throws SQLException {
        Timestamp ts = rs.getTimestamp(index, utc());
        return ts != null ? new LocalDateTime(ts.getTime(), DateTimeZone.UTC) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int index, LocalDateTime value) throws SQLException {
        DateTime dt = value.toDateTime(DateTimeZone.UTC);
        st.setTimestamp(index, new Timestamp(dt.getMillis()), utc());
    }

}
