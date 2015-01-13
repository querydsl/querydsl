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
 * DateType maps Date to Date on the JDBC level
 *
 * @author tiwe
 *
 */
public class DateType extends AbstractDateTimeType<Date> {

    public DateType() {
        super(Types.DATE);
    }

    public DateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Date value) {
        return dateFormatter.print(value.getTime());
    }

    @Override
    public Date getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getDate(startIndex);
    }

    @Override
    public Class<Date> getReturnedClass() {
        return Date.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Date value) throws SQLException {
        st.setDate(startIndex, value);
    }

}
