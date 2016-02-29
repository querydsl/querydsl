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

/**
 *  SQLNoCloseListener can be used to block {@link SQLCloseListener} from closing the connection, useful for
 *  helper query executions
 */
public final class SQLNoCloseListener extends SQLBaseListener {

    public static final SQLNoCloseListener DEFAULT = new SQLNoCloseListener();

    private SQLNoCloseListener() { }

    @Override
    public void start(SQLListenerContext context) {
        context.setData(AbstractSQLQuery.PARENT_CONTEXT, context);
    }

    @Override
    public void end(SQLListenerContext context) {
        context.setData(AbstractSQLQuery.PARENT_CONTEXT, null);
    }

}