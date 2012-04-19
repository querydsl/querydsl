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
package com.mysema.query.sql.mysql;

import java.io.File;
import java.sql.Connection;

import com.google.common.base.Joiner;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;

/**
 * MySQLQuery provides MySQL related extensions to SQLQuery
 * 
 * @author tiwe
 * @see SQLQuery
 *
 */
public class MySQLQuery extends AbstractSQLQuery<MySQLQuery> implements SQLCommonQuery<MySQLQuery>{
    
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
    
    public MySQLQuery bigResult() {
        return addFlag(Position.AFTER_SELECT, "sql_big_result ");
    }
    
    public MySQLQuery bufferResult() {
        return addFlag(Position.AFTER_SELECT, "sql_buffer_result ");
    }
    
    public MySQLQuery cache() {
        return addFlag(Position.AFTER_SELECT, "sql_cache ");
    }
    
    public MySQLQuery calcFoundRows() {
        return addFlag(Position.AFTER_SELECT, "sql_calc_found_rows ");
    }
    
    public MySQLQuery forUpdate() {
        return addFlag(Position.END, "\nfor update ");
    }
    
    public MySQLQuery highPriority() {
        return addFlag(Position.AFTER_SELECT, "high_priority ");
    }
    
    public MySQLQuery into(String var) {
        return addFlag(Position.END, "\ninto " + var);
    }

    public MySQLQuery intoDumpfile(File file) {
        return addFlag(Position.END, "\ninto dumpfile '" + file.getPath() + "'" );
    }
    
    public MySQLQuery intoOutfile(File file) {
        return addFlag(Position.END, "\ninto outfile '" + file.getPath() + "'" );
    }
    
    public MySQLQuery lockInShareMode() {
        return addFlag(Position.END, "\nlock in share mode ");
    }
    
    public MySQLQuery noCache() {
        return addFlag(Position.AFTER_SELECT, "sql_no_cache ");
    }
    
    public MySQLQuery smallResult() {
        return addFlag(Position.AFTER_SELECT, "sql_small_result ");
    }
    
    public MySQLQuery straightJoin() {
        return addFlag(Position.AFTER_SELECT, "straight_join ");
    }
    
    public MySQLQuery useIndex(String... indexes) {
        return addJoinFlag(" use_index (" + JOINER.join(indexes) + ")", JoinFlag.Position.END);
    }
    
    public MySQLQuery withRollup() {
        return addFlag(Position.AFTER_GROUP_BY, "\nwith rollup ");
    }
    
}
