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
import java.sql.SQLException;
import java.sql.Types;

/**
 * IntegerType maps Integer to Integer on the JDBC level
 *
 * @author tiwe
 *
 */
public class IntegerType extends AbstractNumberType<Integer> {

    public IntegerType() {
        super(Types.INTEGER);
    }

    public IntegerType(int type) {
        super(type);
    }

    @Override
    public Class<Integer> getReturnedClass() {
        return Integer.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Integer value) throws SQLException {
        st.setInt(startIndex, value);
    }

}
