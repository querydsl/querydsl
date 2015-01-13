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
package com.querydsl.sql.mysql;

import java.io.File;
import java.sql.Connection;

import com.google.common.base.Joiner;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

/**
 * MySQLQuery provides MySQL related extensions to SQLQuery
 *
 * @author tiwe
 * @see SQLQuery
 *
 */
public class MySQLQuery extends AbstractSQLQuery<MySQLQuery> {

    private static final String WITH_ROLLUP = "\nwith rollup ";

    private static final String STRAIGHT_JOIN = "straight_join ";

    private static final String SQL_SMALL_RESULT = "sql_small_result ";

    private static final String SQL_NO_CACHE = "sql_no_cache ";

    private static final String LOCK_IN_SHARE_MODE = "\nlock in share mode ";

    private static final String HIGH_PRIORITY = "high_priority ";

    private static final String SQL_CALC_FOUND_ROWS = "sql_calc_found_rows ";

    private static final String SQL_CACHE = "sql_cache ";

    private static final String SQL_BUFFER_RESULT = "sql_buffer_result ";

    private static final String SQL_BIG_RESULT = "sql_big_result ";

    private static final Joiner JOINER = Joiner.on(", ");

    public MySQLQuery(Connection conn) {
        this(conn, new Configuration(new MySQLTemplates()), new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public MySQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    /**
     * @return
     */
    public MySQLQuery bigResult() {
        return addFlag(Position.AFTER_SELECT, SQL_BIG_RESULT);
    }

    /**
     * @return
     */
    public MySQLQuery bufferResult() {
        return addFlag(Position.AFTER_SELECT, SQL_BUFFER_RESULT);
    }

    /**
     * @return
     */
    public MySQLQuery cache() {
        return addFlag(Position.AFTER_SELECT, SQL_CACHE);
    }

    /**
     * @return
     */
    public MySQLQuery calcFoundRows() {
        return addFlag(Position.AFTER_SELECT, SQL_CALC_FOUND_ROWS);
    }

    /**
     * @return
     */
    public MySQLQuery highPriority() {
        return addFlag(Position.AFTER_SELECT, HIGH_PRIORITY);
    }

    /**
     * @param var
     * @return
     */
    public MySQLQuery into(String var) {
        return addFlag(Position.END, "\ninto " + var);
    }

    /**
     * @param file
     * @return
     */
    public MySQLQuery intoDumpfile(File file) {
        return addFlag(Position.END, "\ninto dumpfile '" + file.getPath() + "'" );
    }

    /**
     * @param file
     * @return
     */
    public MySQLQuery intoOutfile(File file) {
        return addFlag(Position.END, "\ninto outfile '" + file.getPath() + "'" );
    }

    /**
     * @return
     */
    public MySQLQuery lockInShareMode() {
        return addFlag(Position.END, LOCK_IN_SHARE_MODE);
    }

    /**
     * @return
     */
    public MySQLQuery noCache() {
        return addFlag(Position.AFTER_SELECT, SQL_NO_CACHE);
    }

    /**
     * @return
     */
    public MySQLQuery smallResult() {
        return addFlag(Position.AFTER_SELECT, SQL_SMALL_RESULT);
    }

    /**
     * @return
     */
    public MySQLQuery straightJoin() {
        return addFlag(Position.AFTER_SELECT, STRAIGHT_JOIN);
    }

    /**
     * @param indexes
     * @return
     */
    public MySQLQuery forceIndex(String... indexes) {
        return addJoinFlag(" force index (" + JOINER.join(indexes) + ")", JoinFlag.Position.END);
    }

    /**
     * @param indexes
     * @return
     */
    public MySQLQuery ignoreIndex(String... indexes) {
        return addJoinFlag(" ignore index (" + JOINER.join(indexes) + ")", JoinFlag.Position.END);
    }


    /**
     * @param indexes
     * @return
     */
    public MySQLQuery useIndex(String... indexes) {
        return addJoinFlag(" use index (" + JOINER.join(indexes) + ")", JoinFlag.Position.END);
    }


    /**
     * @return
     */
    public MySQLQuery withRollup() {
        return addFlag(Position.AFTER_GROUP_BY, WITH_ROLLUP);
    }

    @Override
    public MySQLQuery clone(Connection conn) {
        MySQLQuery q = new MySQLQuery(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }
    
}
