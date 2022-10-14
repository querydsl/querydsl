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
package com.querydsl.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

/**
 * {@code UtilDateType} maps Date to Timestamp on the JDBC level
 *
 * @author tiwe
 *
 */
public class UtilDateType extends AbstractDateTimeType<Date> {

    public UtilDateType() {
        super(Types.TIMESTAMP);
    }

    public UtilDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Date value) {
        return dateTimeFormatter.format(new java.sql.Timestamp(value.getTime()).toLocalDateTime());
    }

    @Override
    public Date getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTimestamp(startIndex);
    }

    @Override
    public Class<Date> getReturnedClass() {
        return Date.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Date value) throws SQLException {
        st.setTimestamp(startIndex, new java.sql.Timestamp(value.getTime()));
    }

}
