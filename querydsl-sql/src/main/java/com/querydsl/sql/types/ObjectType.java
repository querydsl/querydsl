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
import java.sql.Types;

/**
 * ObjectType maps Object to Object on the JDBC level
 *
 * @author tiwe
 *
 */
public class ObjectType extends AbstractType<Object> {

    public ObjectType() {
        super(Types.OTHER);
    }

    public ObjectType(int type) {
        super(type);
    }

    @Override
    public Object getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getObject(startIndex);
    }

    @Override
    public Class<Object> getReturnedClass() {
        return Object.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Object value)
            throws SQLException {
        st.setObject(startIndex, value);
    }

}
