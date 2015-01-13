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
import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * LocaleType maps Locale to String on the JDBC level
 *
 * @author tiwe
 *
 */
public class LocaleType extends AbstractType<Locale> {

    private static final Pattern LOCALE = Pattern.compile("[_#-]+");

    public LocaleType() {
        super(Types.VARCHAR);
    }

    public LocaleType(int type) {
        super(type);
    }

    @Override
    public Class<Locale> getReturnedClass() {
        return Locale.class;
    }

    @Override
    @Nullable
    public Locale getValue(ResultSet rs, int startIndex) throws SQLException {
        String val = rs.getString(startIndex);
        return val != null ? toLocale(val) : null;
    }

    public static Locale toLocale(String val) {
        String[] tokens = LOCALE.split(val);
        switch (tokens.length) {
        case 1: return new Locale(tokens[0]);
        case 2: return new Locale(tokens[0], tokens[1]);
        case 3: return new Locale(tokens[0], tokens[1], tokens[2]);
        }
        return null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Locale value) throws SQLException {
        st.setString(startIndex, value.toString());
    }

}
