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

/**
 * TimestampType maps Timestamp to Timestamp on the JDBC level
 *
 * @author tiwe
 *
 */
public class TimestampType extends AbstractType<Timestamp> {

    public TimestampType() {
        super(Types.TIMESTAMP);
    }

    public TimestampType(int type) {
        super(type);
    }

    @Override
    public Timestamp getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getTimestamp(startIndex);
    }

    @Override
    public Class<Timestamp> getReturnedClass() {
        return Timestamp.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Timestamp value) throws SQLException {
        st.setTimestamp(startIndex, value);
    }

}
