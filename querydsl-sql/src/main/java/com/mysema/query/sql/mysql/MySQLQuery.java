/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.mysql;

import java.io.File;
import java.sql.Connection;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * MySQLQuery provides MySQL related extensions to SQLQuery
 * 
 * @author tiwe
 * @see SQLQuery
 *
 */
public class MySQLQuery extends AbstractSQLQuery<MySQLQuery>{
    
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
    
    public MySQLQuery bigResult(){
        return addFlag(Position.AFTER_SELECT, "SQL_BIG_RESULT ");
    }
    
    public MySQLQuery bufferResult(){
        return addFlag(Position.AFTER_SELECT, "SQL_BUFFER_RESULT ");
    }
    
    public MySQLQuery cache(){
        return addFlag(Position.AFTER_SELECT, "SQL_CACHE ");
    }
    
    public MySQLQuery calcFoundRows(){
        return addFlag(Position.AFTER_SELECT, "SQL_CALC_FOUND_ROWS ");
    }
    
    public MySQLQuery forUpdate(){
        return addFlag(Position.END, "\nFOR UPDATE ");
    }
    
    public MySQLQuery highPriority(){
        return addFlag(Position.AFTER_SELECT, "HIGH_PRIORITY ");
    }
    
    public MySQLQuery into(String var){
        return addFlag(Position.END, "\nINTO " + var);
    }

    public MySQLQuery intoDumpfile(File file){
        return addFlag(Position.END, "\nINTO DUMPFILE '" + file.getPath() + "'" );
    }
    
    public MySQLQuery intoOutfile(File file){
        return addFlag(Position.END, "\nINTO OUTFILE '" + file.getPath() + "'" );
    }
    
    public MySQLQuery lockInShareMode(){
        return addFlag(Position.END, "\nLOCK IN SHARE MODE ");
    }
    
    public MySQLQuery noCache(){
        return addFlag(Position.AFTER_SELECT, "SQL_NO_CACHE ");
    }
    
    public MySQLQuery smallResult(){
        return addFlag(Position.AFTER_SELECT, "SQL_SMALL_RESULT ");
    }
    
    public MySQLQuery straightJoin(){
        return addFlag(Position.AFTER_SELECT, "STRAIGHT_JOIN ");
    }
    
    public MySQLQuery useIndex(String... indexes){
        return addJoinFlag(" USE_INDEX (" + StringUtils.join(indexes, ", ") + ")");
    }
    
    public MySQLQuery withRollup(){
        return addFlag(Position.AFTER_GROUP_BY, "\nWITH ROLLUP ");
    }
    
}
