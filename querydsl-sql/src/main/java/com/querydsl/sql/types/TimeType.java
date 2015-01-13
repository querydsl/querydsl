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

import java.sql.*;

/**
 * TimeType maps Time to Time on the JDBC level
 *
 * @author tiwe
 *
 */
public class TimeType extends AbstractDateTimeType<Time> {

    public TimeType() {
        super(Types.TIME);
    }

    public TimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Time value) {
        return timeFormatter.print(value.getTime());
    }

    @Override
    public Time getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTime(startIndex);
    }

    @Override
    public Class<Time> getReturnedClass() {
        return Time.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Time value) throws SQLException {
        st.setTime(startIndex, value);
    }

}
