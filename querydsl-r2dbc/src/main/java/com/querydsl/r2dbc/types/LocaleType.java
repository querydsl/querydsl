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
package com.querydsl.r2dbc.types;

import io.r2dbc.spi.Row;
import org.jetbrains.annotations.Nullable;

import java.sql.Types;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * {@code LocaleType} maps Locale to String on the JDBC level
 *
 * @author mc_fish
 */
public class LocaleType extends AbstractType<Locale, String> {

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
    public Locale getValue(Row row, int startIndex) {
        String val = row.get(startIndex, String.class);
        return val != null ? toLocale(val) : null;
    }

    public static Locale toLocale(String val) {
        String[] tokens = LOCALE.split(val);
        switch (tokens.length) {
            case 1:
                return new Locale(tokens[0]);
            case 2:
                return new Locale(tokens[0], tokens[1]);
            case 3:
                return new Locale(tokens[0], tokens[1], tokens[2]);
        }
        return null;
    }

    @Override
    protected String toDbValue(Locale value) {
        return value.toString();
    }

    @Override
    public Class<String> getDatabaseClass() {
        return String.class;
    }

}
