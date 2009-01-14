/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryBase;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.SqlSerializer;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;

/**
 * SqlQuery provides
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlQuery extends QueryBase<Object,SqlQuery>{
    
    private static final Logger logger = LoggerFactory.getLogger(SqlQuery.class);
    
    private String queryString;
    
    private List<Object> constants;
    
    private final Connection conn;
    
    private final SqlOps ops;
    
    public SqlQuery(Connection conn, SqlOps ops){
        this.conn = conn;
        this.ops = ops;
    }
        
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest) throws SQLException{
        select(expr1, expr2);
        select(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants){
            stmt.setObject(counter++, o);    
        }
        ResultSet rs = stmt.executeQuery();   
        try{
            List<Object[]> rv = new ArrayList<Object[]>();
            while(rs.next()){
                // TODO : take constructors into account
                Object[] objects = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++){
                    objects[i] = rs.getObject(i+1);
                }
                rv.add(objects);
            }
            return rv;
        }finally{
            try{
                rs.close();    
            }finally{
                stmt.close();    
            }                        
        }
    }
    
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) throws SQLException{
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants){
            stmt.setObject(counter++, o);    
        }        
        ResultSet rs = stmt.executeQuery();        
        try{
            List<RT> rv = new ArrayList<RT>();
            if (expr instanceof Constructor){                
                Constructor<RT> c = (Constructor<RT>)expr;
                java.lang.reflect.Constructor<RT> cc =  c.getJavaConstructor();
                while (rs.next()){
                    List args = new ArrayList();
                    for (int i=0; i < c.getArgs().length; i++){
                        args.add(rs.getObject(i+1));
                    }
                    try {
                        rv.add(cc.newInstance(args.toArray()));
                    } catch (Exception e) {
                        String error = "Caught " + e.getClass().getName();
                        logger.error(error, e);
                        throw new RuntimeException(error, e);
                    }
                }
            }else{
                while(rs.next()){
                    rv.add((RT)rs.getObject(1));
                }    
            }            
            return rv;
        }finally{
            try{
                rs.close();    
            }finally{
                stmt.close();
            }            
        }
    }
    
    @Override
    public String toString(){
        if (queryString == null){
            queryString = buildQueryString();    
        }        
        return queryString;
    }

    private String buildQueryString() {
        if (joins.isEmpty()){
            throw new IllegalArgumentException("No where clause given");
        }
        SqlSerializer serializer = new SqlSerializer(ops);
        serializer.serialize(select, joins, where.self(), groupBy, having.self(), orderBy, false);               
        constants = serializer.getConstants();      
        return serializer.toString();
    }

}
