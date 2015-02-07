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
package com.querydsl.sql;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;

/**
 * SQLOps provides SQL specific operators
 *
 * @author tiwe
 *
 */
public enum SQLOps implements Operator {
    ALL,
    CAST,
    CORR,
    COVARPOP,
    COVARSAMP,
    CUMEDIST,
    CUMEDIST2,
    DENSERANK,
    DENSERANK2,
    FIRSTVALUE,
    FOR_SHARE,
    FOR_UPDATE,
    LAG,
    LASTVALUE,
    LEAD,
    LISTAGG,
    NEXTVAL,
    NO_WAIT,
    NTHVALUE,
    NTILE,
    PERCENTRANK,
    PERCENTRANK2,
    PERCENTILECONT,
    PERCENTILEDISC,
    QUALIFY,
    RANK,
    RANK2,
    REGR_SLOPE,
    REGR_INTERCEPT,
    REGR_COUNT,
    REGR_R2,
    REGR_AVGX,
    REGR_AVGY,
    REGR_SXX,
    REGR_SYY,
    REGR_SXY,
    RATIOTOREPORT,
    ROWNUMBER,
    STDDEV,
    STDDEVPOP,
    STDDEVSAMP,
    STDDEV_DISTINCT,
    UNION,
    UNION_ALL,
    VARIANCE,
    VARPOP,
    VARSAMP,
    WITH_ALIAS,
    WITH_COLUMNS;

    public static final QueryFlag FOR_SHARE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_SHARE, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag FOR_UPDATE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_UPDATE, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag NO_WAIT_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, NO_WAIT, ImmutableList.<Expression<?>>of()));

}
