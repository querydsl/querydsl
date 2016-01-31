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
package com.querydsl.sql;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operator;

/**
 * {@code SQLOps} provides SQL specific operators
 *
 * @author tiwe
 *
 */
public enum SQLOps implements Operator {
    ALL(Object.class),
    CAST(Object.class),
    CORR(Double.class),
    COVARPOP(Double.class),
    COVARSAMP(Double.class),
    CUMEDIST(Double.class),
    CUMEDIST2(Double.class),
    DENSERANK(Long.class),
    DENSERANK2(Long.class),
    FIRSTVALUE(Object.class),
    FOR_SHARE(Object.class),
    FOR_UPDATE(Object.class),
    LAG(Object.class),
    LASTVALUE(Object.class),
    LEAD(Object.class),
    LISTAGG(Object.class),
    NEXTVAL(Object.class),
    NO_WAIT(Object.class),
    NTHVALUE(Object.class),
    NTILE(Object.class),
    PERCENTRANK(Double.class),
    PERCENTRANK2(Double.class),
    PERCENTILECONT(Object.class),
    PERCENTILEDISC(Object.class),
    QUALIFY(Boolean.class),
    RANK(Long.class),
    RANK2(Long.class),
    REGR_SLOPE(Object.class),
    REGR_INTERCEPT(Object.class),
    REGR_COUNT(Object.class),
    REGR_R2(Object.class),
    REGR_AVGX(Object.class),
    REGR_AVGY(Object.class),
    REGR_SXX(Object.class),
    REGR_SYY(Object.class),
    REGR_SXY(Object.class),
    RATIOTOREPORT(Object.class),
    ROWNUMBER(Long.class),
    STDDEV(Object.class),
    STDDEVPOP(Object.class),
    STDDEVSAMP(Object.class),
    STDDEV_DISTINCT(Object.class),
    UNION(Object.class),
    UNION_ALL(Object.class),
    VARIANCE(Object.class),
    VARPOP(Object.class),
    VARSAMP(Object.class),
    WITH_ALIAS(Object.class),
    WITH_COLUMNS(Object.class),
    LOCK_IN_SHARE_MODE(Object.class),
    WITH_REPEATABLE_READ(Object.class),
    GROUP_CONCAT(String.class),
    GROUP_CONCAT2(String.class);

    private final Class<?> type;

    private SQLOps(Class<?> type) {
        this.type = type;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Deprecated
    public static final QueryFlag FOR_SHARE_FLAG = new QueryFlag(Position.END, ExpressionUtils.operation(
            Object.class, FOR_SHARE, ImmutableList.<Expression<?>>of()));
    @Deprecated
    public static final QueryFlag FOR_UPDATE_FLAG = new QueryFlag(Position.END, ExpressionUtils.operation(
            Object.class, FOR_UPDATE, ImmutableList.<Expression<?>>of()));
    @Deprecated
    public static final QueryFlag NO_WAIT_FLAG = new QueryFlag(Position.END, ExpressionUtils.operation(
            Object.class, NO_WAIT, ImmutableList.<Expression<?>>of()));

}
