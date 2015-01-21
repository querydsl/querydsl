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

import javax.annotation.Nullable;

import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Templates;

/**
 * JPQLTemplates extends {@link Templates} to provide operator patterns for JPQL
 * serialization
 *
 * @author tiwe
 * @see HQLTemplates
 * @see EclipseLinkTemplates
 */
public class JPQLTemplates extends Templates {

    public static final char DEFAULT_ESCAPE = '!';

    public static final JPQLTemplates DEFAULT = new JPQLTemplates();

    private final QueryHandler queryHandler;

    protected JPQLTemplates() {
        this(DEFAULT_ESCAPE, DefaultQueryHandler.DEFAULT);
    }

    protected JPQLTemplates(char escape) {
        this(escape, DefaultQueryHandler.DEFAULT);
    }

    protected JPQLTemplates(char escape, QueryHandler queryHandler) {
        super(escape);
        this.queryHandler = queryHandler;

        add(Ops.CASE, "case {0} end");
        add(Ops.CASE_WHEN,  "when {0} then {1} {2}", 0);
        add(Ops.CASE_ELSE,  "else {0}", 0);

        //CHECKSTYLE:OFF
        // boolean
        add(Ops.AND, "{0} and {1}", 36);
        add(Ops.NOT, "not {0}", 3);
        add(Ops.OR, "{0} or {1}", 38);
        add(Ops.XNOR, "{0} xnor {1}", 39);
        add(Ops.XOR, "{0} xor {1}", 39);

        // comparison
        add(Ops.BETWEEN, "{0} between {1} and {2}", 30);

        // numeric
        add(Ops.MathOps.SQRT, "sqrt({0})");
        add(Ops.MOD, "mod({0},{1})", 0);

        // various
        add(Ops.NE, "{0} <> {1}", 25);
        add(Ops.IS_NULL, "{0} is null", 26);
        add(Ops.IS_NOT_NULL, "{0} is not null", 26);
        add(JPQLOps.CAST, "cast({0} as {1s})");
        add(Ops.NUMCAST, "cast({0} as {1s})");

        // collection
        add(JPQLOps.MEMBER_OF, "{0} member of {1}");
        add(JPQLOps.NOT_MEMBER_OF, "{0} not member of {1}");

        add(Ops.IN, "{0} in {1}");
        add(Ops.NOT_IN, "{0} not in {1}");
        add(Ops.COL_IS_EMPTY, "{0} is empty");
        add(Ops.COL_SIZE, "size({0})");
        add(Ops.ARRAY_SIZE, "size({0})");

        // string
        add(Ops.LIKE, "{0} like {1} escape '"+escape+"'",1);
        add(Ops.CONCAT, "concat({0},{1})",0);
        add(Ops.MATCHES, "{0} like {1}  escape '"+escape+"'", 27); // TODO : support real regexes
        add(Ops.MATCHES_IC, "{0} like {1} escape '"+escape+"'", 27); // TODO : support real regexes
        add(Ops.LOWER, "lower({0})");
        add(Ops.SUBSTR_1ARG, "substring({0},{1s}+1)", 1);
        add(Ops.SUBSTR_2ARGS, "substring({0},{1s}+1,{2s}-{1s})", 1);
        add(Ops.TRIM, "trim({0})");
        add(Ops.UPPER, "upper({0})");
        add(Ops.EQ_IGNORE_CASE, "{0l} = {1l}");
        add(Ops.CHAR_AT, "cast(substring({0},{1s}+1,1) as char)");
        add(Ops.STRING_IS_EMPTY, "length({0}) = 0");

        add(Ops.STRING_CONTAINS, "{0} like {%1%} escape '"+escape+"'");
        add(Ops.STRING_CONTAINS_IC, "{0l} like {%%1%%} escape '"+escape+"'");
        add(Ops.ENDS_WITH, "{0} like {%1} escape '"+escape+"'");
        add(Ops.ENDS_WITH_IC, "{0l} like {%%1} escape '"+escape+"'");
        add(Ops.STARTS_WITH, "{0} like {1%} escape '"+escape+"'");
        add(Ops.STARTS_WITH_IC, "{0l} like {1%%} escape '"+escape+"'");
        add(Ops.INDEX_OF, "locate({1},{0})-1");
        add(Ops.INDEX_OF_2ARGS, "locate({1},{0},{2s}+1)-1");

        // date time
        add(Ops.DateTimeOps.SYSDATE, "sysdate");
        add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        add(Ops.DateTimeOps.CURRENT_TIME, "current_time");
        add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp");

        add(Ops.DateTimeOps.MILLISECOND, "0"); // NOT supported in HQL
        add(Ops.DateTimeOps.SECOND, "second({0})");
        add(Ops.DateTimeOps.MINUTE, "minute({0})");
        add(Ops.DateTimeOps.HOUR, "hour({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
        add(Ops.DateTimeOps.MONTH, "month({0})");
        add(Ops.DateTimeOps.YEAR, "year({0})");

        add(Ops.DateTimeOps.YEAR_MONTH, "year({0}) * 100 + month({0})");

        // path types
        add(PathType.PROPERTY, "{0}.{1s}");
        add(PathType.VARIABLE, "{0s}");

        // case for eq
        add(Ops.CASE_EQ, "case {1} end");
        add(Ops.CASE_EQ_WHEN,  "when {0} = {1} then {2} {3}");
        add(Ops.CASE_EQ_ELSE,  "else {0}");

        add(Ops.INSTANCE_OF, "type({0}) = {1}");
        add(JPQLOps.TYPE, "type({0})");
        add(JPQLOps.INDEX, "index({0})");
        add(JPQLOps.TREAT, "treat({0} as {1s})");
        add(JPQLOps.KEY, "key({0})");
        add(JPQLOps.VALUE, "value({0})");

        //CHECKSTYLE:ON
    }

    public boolean wrapElements(Operator<?> operator) {
        return false;
    }

    public boolean isTypeAsString() {
        // TODO : get rid of this when Hibernate supports type(alias)
        return false;
    }

    public String getTypeForCast(Class<?> cl) {
        return cl.getSimpleName().toLowerCase();
    }

    public boolean isEnumInPathSupported() {
        return true;
    }

    public boolean isPathInEntitiesSupported() {
        return true;
    }

    public boolean isSelect1Supported() {
        return false;
    }

    @Nullable
    public String getExistsProjection() {
        return null;
    }

    public boolean wrapConstant(Object constant) {
        // related : https://hibernate.onjira.com/browse/HHH-6913
        return false;
    }

    public boolean isWithForOn() {
        return false;
    }

    public QueryHandler getQueryHandler() {
        return queryHandler;
    }

    public boolean isCaseWithLiterals() {
        return false;
    }

}
