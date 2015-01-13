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
package com.querydsl.jpa;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Ops;

/**
 * EclipseLinkTemplates extends JPQLTemplates with EclipseLink specific extensions
 *
 * @author tiwe
 *
 */
public class EclipseLinkTemplates extends JPQLTemplates {

    private static final QueryHandler QUERY_HANDLER;

    static {
        QueryHandler instance;
        try {
            instance = (QueryHandler) Class.forName("com.querydsl.jpa.EclipseLinkHandler").newInstance();
        } catch (NoClassDefFoundError e) {
            instance = DefaultQueryHandler.DEFAULT;
        } catch (Exception e) {
            instance = DefaultQueryHandler.DEFAULT;
        }
        QUERY_HANDLER = instance;
    }


    public static final EclipseLinkTemplates DEFAULT = new EclipseLinkTemplates();

    private final Map<Class<?>, String> typeNames;

    public EclipseLinkTemplates() {
        this(DEFAULT_ESCAPE);
    }

    public EclipseLinkTemplates(char escape) {
        super(escape, QUERY_HANDLER);

        ImmutableMap.Builder<Class<?>, String> builder = ImmutableMap.builder();
        builder.put(Short.class, "short");
        builder.put(Integer.class, "integer");
        builder.put(Long.class, "bigint");
        builder.put(BigInteger.class, "bigint");
        builder.put(Float.class, "float");
        builder.put(Double.class, "double");
        builder.put(BigDecimal.class, "double");
        typeNames = builder.build();

        add(Ops.CHAR_AT, "substring({0},{1}+1,1)");
        add(JPQLOps.CAST, "cast({0} {1s})");
        add(Ops.STRING_CAST, "cast({0} varchar(255))");
        add(Ops.NUMCAST, "cast({0} {1s})");

        // datetime
        add(Ops.DateTimeOps.MILLISECOND, "extract(millisecond from {0})");
        add(Ops.DateTimeOps.SECOND, "extract(second from {0})");
        add(Ops.DateTimeOps.MINUTE, "extract(minute from {0})");
        add(Ops.DateTimeOps.HOUR, "extract(hour from {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(day_of_week from {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "extract(day from {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(day_of_year from {0})");
        add(Ops.DateTimeOps.WEEK, "extract(week from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");

        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})");

    }

    @Override
    public String getTypeForCast(Class<?> cl) {
        return typeNames.get(cl);
    }

    @Override
    public boolean isPathInEntitiesSupported() {
        return false;
    }

    @Override
    public boolean isSelect1Supported() {
        return true;
    }


}
