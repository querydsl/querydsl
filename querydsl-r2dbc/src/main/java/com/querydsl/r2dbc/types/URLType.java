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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Types;

/**
 * {@code URLType} maps URL to URL on the JDBC level
 *
 * @author tiwe
 */
public class URLType extends AbstractType<URL> {

    public URLType() {
        super(Types.VARCHAR);
    }

    public URLType(int type) {
        super(type);
    }

    @Override
    public URL getValue(Row row, int startIndex) {
        String val = row.get(startIndex, String.class);
        try {
            return val != null ? new URL(val) : null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public Class<URL> getReturnedClass() {
        return URL.class;
    }

    @Override
    protected Object toDbValue(URL value) {
        return value.toString();
    }

}
