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
package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.DateTime;

/**
 * DateTimeType maps DateTime to Timestamp on the JDBC level
 *
 * @author tiwe
 *
 */
public class DateTimeType extends AbstractType<DateTime> {

    public DateTimeType() {
        super(Types.TIMESTAMP);
    }

    public DateTimeType(int type) {
        super(type);
    }

    @Override
    public Class<DateTime> getReturnedClass() {
        return DateTime.class;
    }

    @Override
    public DateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(startIndex);
        return ts != null ? new DateTime(ts.getTime()) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, DateTime value) throws SQLException {
        st.setTimestamp(startIndex, new Timestamp(value.getMillis()));
    }

}
