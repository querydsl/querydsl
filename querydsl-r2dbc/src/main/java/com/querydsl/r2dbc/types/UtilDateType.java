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

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * {@code UtilDateType} maps Date to Timestamp on the JDBC level
 *
 * @author tiwe
 */
public class UtilDateType extends AbstractDateTimeType<Date> {

    public UtilDateType() {
        super(Types.TIMESTAMP);
    }

    public UtilDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Date value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<Date> getReturnedClass() {
        return Date.class;
    }

    @Override
    protected Object toDbValue(Date value) {
        return new Timestamp(value.getTime());
    }

}
