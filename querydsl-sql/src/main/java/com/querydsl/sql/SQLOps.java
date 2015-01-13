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
import com.querydsl.core.types.OperatorImpl;

/**
 * SQLOps provides SQL specific operators
 *
 * @author tiwe
 *
 */
public final class SQLOps {

    private static final String NS = SQLOps.class.getName();

    public static final Operator<Object> ALL = new OperatorImpl<Object>(NS, "ALL");

    public static final Operator<Object> CAST = new OperatorImpl<Object>(NS, "CAST");

    public static final Operator<Double> CORR = new OperatorImpl<Double>(NS, "CORR");

    public static final Operator<Double> COVARPOP = new OperatorImpl<Double>(NS, "COVARPOP");

    public static final Operator<Double> COVARSAMP = new OperatorImpl<Double>(NS, "COVARSAMP");

    public static final Operator<Double> CUMEDIST = new OperatorImpl<Double>(NS, "CUMEDIST");

    public static final Operator<Double> CUMEDIST2 = new OperatorImpl<Double>(NS, "CUMEDIST2");

    public static final Operator<Long> DENSERANK = new OperatorImpl<Long>(NS, "DENSERANK");

    public static final Operator<Long> DENSERANK2 = new OperatorImpl<Long>(NS, "DENSERANK2");

    public static final Operator<Object> FIRSTVALUE = new OperatorImpl<Object>(NS, "FIRSTVALUE");

    public static final Operator<Object> FOR_SHARE = new OperatorImpl<Object>(NS, "FOR_SHARE");

    public static final Operator<Object> FOR_UPDATE = new OperatorImpl<Object>(NS, "FOR_UPDATE");

    public static final Operator<Object> LAG = new OperatorImpl<Object>(NS, "LAG");

    public static final Operator<Object> LASTVALUE = new OperatorImpl<Object>(NS, "LASTVALUE");

    public static final Operator<Object> LEAD = new OperatorImpl<Object>(NS, "LEAD");

    public static final Operator<Object> LISTAGG = new OperatorImpl<Object>(NS, "LISTAGG");

    public static final Operator<Object> NEXTVAL = new OperatorImpl<Object>(NS, "NEXTVAL");

    public static final Operator<Object> NO_WAIT = new OperatorImpl<Object>(NS, "NO_WAIT");

    public static final Operator<Object> NTHVALUE = new OperatorImpl<Object>(NS, "NTHVALUE");

    public static final Operator<Object> NTILE = new OperatorImpl<Object>(NS, "NTILE");

    public static final Operator<Double> PERCENTRANK = new OperatorImpl<Double>(NS, "PERCENTRANK");

    public static final Operator<Double> PERCENTRANK2 = new OperatorImpl<Double>(NS, "PERCENTRANK2");

    public static final Operator<Object> PERCENTILECONT = new OperatorImpl<Object>(NS, "PERCENTILECONT");

    public static final Operator<Object> PERCENTILEDISC = new OperatorImpl<Object>(NS, "PERCENTILEDISC");

    public static final Operator<Boolean> QUALIFY = new OperatorImpl<Boolean>(NS, "QUALIFY");

    public static final Operator<Long> RANK = new OperatorImpl<Long>(NS, "RANK");

    public static final Operator<Long> RANK2 = new OperatorImpl<Long>(NS, "RANK2");

    public static final Operator<Object> REGR_SLOPE = new OperatorImpl<Object>(NS, "REGR_SLOPE");

    public static final Operator<Object> REGR_INTERCEPT = new OperatorImpl<Object>(NS, "REGR_INTERCEPT");

    public static final Operator<Object> REGR_COUNT = new OperatorImpl<Object>(NS, "REGR_COUNT");

    public static final Operator<Object> REGR_R2 = new OperatorImpl<Object>(NS, "REGR_R2");

    public static final Operator<Object> REGR_AVGX = new OperatorImpl<Object>(NS, "REGR_AVGX");

    public static final Operator<Object> REGR_AVGY = new OperatorImpl<Object>(NS, "REGR_AVGY");

    public static final Operator<Object> REGR_SXX = new OperatorImpl<Object>(NS, "REGR_SXX");

    public static final Operator<Object> REGR_SYY = new OperatorImpl<Object>(NS, "REGR_SYY");

    public static final Operator<Object> REGR_SXY = new OperatorImpl<Object>(NS, "REGR_SXY");

    public static final Operator<Object> RATIOTOREPORT = new OperatorImpl<Object>(NS, "RATIOTOREPORT");

    public static final Operator<Long> ROWNUMBER = new OperatorImpl<Long>(NS, "ROWNUMBER");

    public static final Operator<Object> STDDEV = new OperatorImpl<Object>(NS, "STDDEV");

    public static final Operator<Object> STDDEVPOP = new OperatorImpl<Object>(NS, "STDDEVPOP");

    public static final Operator<Object> STDDEVSAMP = new OperatorImpl<Object>(NS, "STDDEVSAMP");

    public static final Operator<Object> STDDEV_DISTINCT = new OperatorImpl<Object>(NS, "STDDEV_DISTINCT");

    public static final Operator<Object> UNION = new OperatorImpl<Object>(NS, "UNION");

    public static final Operator<Object> UNION_ALL = new OperatorImpl<Object>(NS, "UNION_ALL");

    public static final Operator<Object> VARIANCE = new OperatorImpl<Object>(NS, "VARIANCE");

    public static final Operator<Object> VARPOP = new OperatorImpl<Object>(NS, "VARPOP");

    public static final Operator<Object> VARSAMP = new OperatorImpl<Object>(NS, "VARSAMP");

    public static final Operator<Object> WITH_ALIAS = new OperatorImpl<Object>(NS, "WITH_ALIAS");

    public static final Operator<Object> WITH_COLUMNS = new OperatorImpl<Object>(NS, "WITH_COLUMNS");

    public static final QueryFlag FOR_SHARE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_SHARE, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag FOR_UPDATE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_UPDATE, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag NO_WAIT_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, NO_WAIT, ImmutableList.<Expression<?>>of()));

    private SQLOps() {}

}
