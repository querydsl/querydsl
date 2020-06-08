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

/**
 * {@code TimestampType} maps Timestamp to Timestamp on the JDBC level
 *
 * @author tiwe
 */
public class TimestampType extends AbstractDateTimeType<Timestamp> {

    public TimestampType() {
        super(Types.TIMESTAMP);
    }

    public TimestampType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(Timestamp value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<Timestamp> getReturnedClass() {
        return Timestamp.class;
    }

}
