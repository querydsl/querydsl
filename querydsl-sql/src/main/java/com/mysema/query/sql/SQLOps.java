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
package com.mysema.query.sql;

import com.google.common.collect.ImmutableList;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;

/**
 * SQLOps provides SQL specific operators
 *
 * @author tiwe
 *
 */
public final class SQLOps {

    private static final String NS = SQLOps.class.getName();

    public static final Operator<Object> CAST = new OperatorImpl<Object>(NS, "CAST");

    public static final Operator<Object> UNION = new OperatorImpl<Object>(NS, "UNION");

    public static final Operator<Object> UNION_ALL = new OperatorImpl<Object>(NS, "UNION_ALL");

    public static final Operator<Object> NEXTVAL = new OperatorImpl<Object>(NS, "NEXTVAL");

    public static final Operator<Long> ROWNUMBER = new OperatorImpl<Long>(NS, "ROWNUMBER");

    public static final Operator<Long> RANK = new OperatorImpl<Long>(NS, "RANK");

    public static final Operator<Long> DENSERANK = new OperatorImpl<Long>(NS, "DENSERANK");

    public static final Operator<Object> FIRSTVALUE = new OperatorImpl<Object>(NS, "FIRSTVALUE");

    public static final Operator<Object> LASTVALUE = new OperatorImpl<Object>(NS, "LASTVALUE");

    public static final Operator<Object> LEAD = new OperatorImpl<Object>(NS, "LEAD");

    public static final Operator<Object> LAG = new OperatorImpl<Object>(NS, "LAG");

    public static final Operator<Object> WITH_ALIAS = new OperatorImpl<Object>(NS, "WITH_ALIAS");

    public static final Operator<Object> WITH_COLUMNS = new OperatorImpl<Object>(NS, "WITH_COLUMNS");

    public static final Operator<Object> NO_WAIT = new OperatorImpl<Object>(NS, "NO_WAIT");

    public static final Operator<Object> FOR_UPDATE = new OperatorImpl<Object>(NS, "FOR_UPDATE");

    public static final Operator<Object> FOR_SHARE = new OperatorImpl<Object>(NS, "FOR_SHARE");

    public static final QueryFlag NO_WAIT_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, NO_WAIT, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag FOR_UPDATE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_UPDATE, ImmutableList.<Expression<?>>of()));

    public static final QueryFlag FOR_SHARE_FLAG = new QueryFlag(Position.END, new OperationImpl<Object>(
            Object.class, FOR_SHARE, ImmutableList.<Expression<?>>of()));

    private SQLOps() {}

}
