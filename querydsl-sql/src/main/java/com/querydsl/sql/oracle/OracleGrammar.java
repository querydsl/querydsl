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
package com.querydsl.sql.oracle;

import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import java.util.Date;

/**
 * Convenience functions and constants for Oracle DB usage
 *
 * @author tiwe
 */
public final class OracleGrammar {

    private OracleGrammar() { }

    public static final NumberExpression<Integer> level = Expressions.numberTemplate(Integer.class, "level");

    public static final NumberExpression<Integer> rownum = Expressions.numberTemplate(Integer.class, "rownum");

    public static final NumberExpression<Integer> rowid = Expressions.numberTemplate(Integer.class, "rowid");

    public static final DateExpression<Date> sysdate = Expressions.dateTemplate(Date.class, "sysdate");
}
