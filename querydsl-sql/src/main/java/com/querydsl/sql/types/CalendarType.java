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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

/**
 * CalendarType maps Calendar to Timestamp on the JDBC level
 *
 * @author tiwe
 *
 */
public class CalendarType extends AbstractDateTimeType<Calendar> {

    public CalendarType() {
        super(Types.TIMESTAMP);
    }

    public CalendarType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Calendar value) {
        return dateTimeFormatter.print(value.getTimeInMillis());
    }

    @Override
    public Calendar getValue(ResultSet rs, int startIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(startIndex);
        if (ts != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(ts.getTime());
            return cal;
        } else {
            return null;
        }
    }

    @Override
    public Class<Calendar> getReturnedClass() {
        return Calendar.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Calendar value) throws SQLException {
        st.setTimestamp(startIndex, new Timestamp(value.getTimeInMillis()));
    }

}
