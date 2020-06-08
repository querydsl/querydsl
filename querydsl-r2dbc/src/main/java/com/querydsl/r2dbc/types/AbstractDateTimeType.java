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


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Common abstract superclass for Type implementations
 *
 * @param <T>
 * @author tiwe
 */
public abstract class AbstractDateTimeType<T> extends AbstractType<T> {

    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected static Calendar utc() {
        return (Calendar) UTC.clone();
    }

    protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    protected static final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    public AbstractDateTimeType(int type) {
        super(type);
    }

}
