/*
 * Copyright 2013, Mysema Ltd
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
import java.util.UUID;

/**
 * UtilUUIDType maps UUID to String on the JDBC level
 *
 * @author tiwe
 *
 */
public class UtilUUIDType extends AbstractType<UUID> {

    private final boolean asString;

    public UtilUUIDType() {
        this(Types.OTHER, true);
    }

    public UtilUUIDType(boolean asString) {
        this(Types.OTHER, asString);
    }

    public UtilUUIDType(int type) {
        this(type, true);
    }

    public UtilUUIDType(int type, boolean asString) {
        super(type);
        this.asString = asString;
    }

    @Override
    public UUID getValue(ResultSet rs, int startIndex) throws SQLException {
        if (asString) {
            String str = rs.getString(startIndex);
            return str != null ? UUID.fromString(str) : null;
        } else {
            return (UUID) rs.getObject(startIndex);
        }
    }

    @Override
    public Class<UUID> getReturnedClass() {
        return UUID.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, UUID value) throws SQLException {
        if (asString) {
            st.setString(startIndex, value.toString());
        } else {
            st.setObject(startIndex, value);
        }

    }
}