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
package com.querydsl.sql.types;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Common abstract superclass for Type implementations
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractDateTimeType<T>  extends AbstractType<T> {

    private static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    static {
        UTC.setTimeInMillis(0);
    }

    protected static Calendar utc() {
        return (Calendar) UTC.clone();
    }

    protected static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    protected static final DateTimeFormatter dateTimeOffsetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx");
    
    protected static final DateTimeFormatter dateTimeZoneFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");

    protected static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    protected static final DateTimeFormatter timeOffsetFormatter = DateTimeFormatter.ofPattern("HH:mm:ss xxx");

    public AbstractDateTimeType(int type) {
        super(type);
    }

}
