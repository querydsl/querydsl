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
import java.util.Currency;

import javax.annotation.Nullable;

/**
 * CurrencyType maps Currency to String on the JDBC level
 *
 * @author tiwe
 *
 */
public class CurrencyType extends AbstractType<Currency> {

    public CurrencyType() {
        super(Types.VARCHAR);
    }

    public CurrencyType(int type) {
        super(type);
    }

    @Override
    public Class<Currency> getReturnedClass() {
        return Currency.class;
    }

    @Override
    @Nullable
    public Currency getValue(ResultSet rs, int startIndex) throws SQLException {
        String val = rs.getString(startIndex);
        return val != null ? Currency.getInstance(val) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Currency value) throws SQLException {
        st.setString(startIndex, value.getCurrencyCode());
    }

}
