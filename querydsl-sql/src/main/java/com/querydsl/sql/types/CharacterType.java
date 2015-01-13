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
 * CharacterType maps Character to Character on the JDBC level
 *
 * @author tiwe
 *
 */
public class CharacterType extends AbstractType<Character> {

    public CharacterType() {
        super(Types.CHAR);
    }

    public CharacterType(int type) {
        super(type);
    }

    @Override
    public Character getValue(ResultSet rs, int startIndex) throws SQLException {
        String str = rs.getString(startIndex);
        return str != null ? str.charAt(0) : null;
    }

    @Override
    public Class<Character> getReturnedClass() {
        return Character.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Character value)
            throws SQLException {
        st.setString(startIndex, value.toString());
    }

}
