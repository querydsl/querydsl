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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryBase;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.SqlJoinMeta;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.SqlSerializer;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.SubQuery;

/**
 * SqlQuery is a JDBC based implementation of the Querydsl Query interface
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlQuery extends QueryBase<SqlJoinMeta,SqlQuery>{
    
    private static final Logger logger = LoggerFactory.getLogger(SqlQuery.class);
    
    private String queryString;
    
    private int limit, offset;
    
    private List<Object> constants;
    
    private final Connection conn;
    
    private final SqlOps ops;
    
    private boolean forCountRow = false;
    
    private SubQuery<SqlJoinMeta, ?>[] sq;
    
    public SqlQuery(Connection conn, SqlOps ops){
        this.conn = conn;
        this.ops = ops;
    }
        
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest) throws SQLException{
        select(expr1, expr2);
        select(rest);
        return listMultiple();
    }
    
    private List<Object[]> listMultiple() throws SQLException{
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
        
    public <RT extends Comparable<RT>> List<RT> list(Expr<RT> expr) throws SQLException{
        select(expr);
        return listSingle(expr);
    }
    
    public <RT> List<RT> list(Constructor<RT> expr) throws SQLException{
        select(expr);
        return listSingle(expr);
    }
    
    private <RT> List<RT> listSingle(Expr<RT> expr) throws SQLException{        
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants){ 
            try {
                set(stmt, counter++,o);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(error, e);
            }
        }        
        ResultSet rs = stmt.executeQuery();        
        try{
            List<RT> rv = new ArrayList<RT>();
            if (expr instanceof Constructor){                
                Constructor<RT> c = (Constructor<RT>)expr;
                java.lang.reflect.Constructor<RT> cc =  c.getJavaConstructor();
                while (rs.next()){                    
                    try {
                        List<Object> args = new ArrayList<Object>();
                        for (int i=0; i < c.getArgs().length; i++){                        
                            args.add(get(rs,i+1,c.getArgs()[i].getType()));
                        }
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
    
    @SuppressWarnings("unchecked")
    private <T> T get(ResultSet rs, int i, Class<T> type) throws Exception {
        String methodName = "get"+type.getSimpleName();
        if (methodName.equals("getInteger")){
            methodName = "getInt";
        }
        // TODO : cache methods
        return (T)ResultSet.class.getMethod(methodName, int.class).invoke(rs, i);
    }
    
    private void set(PreparedStatement stmt, int i, Object o) throws Exception {
        Class<?> type = o.getClass();
        String methodName = "set"+type.getSimpleName();
        if (methodName.equals("setInteger")){
            methodName = "setInt";
        }
        type = ClassUtils.wrapperToPrimitive(type) != null ? ClassUtils.wrapperToPrimitive(type) : type;
        if (methodName.equals("setDate") && type.equals(java.util.Date.class)){
            type = java.sql.Date.class;
            o = new java.sql.Date(((java.util.Date)o).getTime());
        }               
        // TODO : cache methods
        PreparedStatement.class.getMethod(methodName, int.class, type).invoke(stmt, i, o);
    }

    @Override
    public String toString(){
        if (queryString == null){
            queryString = buildQueryString();    
        }        
        return queryString;
    }
    
    @SuppressWarnings("unchecked")
    public <RT> UnionBuilder<RT> union(SubQuery<SqlJoinMeta,RT>... sq){
        if (!joins.isEmpty()) throw new IllegalArgumentException("Don't mix union and from");
        this.sq = sq;
        return new UnionBuilder();
    }

    protected String buildQueryString() {
        SqlSerializer serializer = new SqlSerializer(ops);
        if (sq != null){
            serializer.serializeUnion(select, sq, where.self(), orderBy);
        }else{
            serializer.serialize(select, joins, where.self(), groupBy, having.self(), orderBy, limit, offset, forCountRow);    
        }                       
        constants = serializer.getConstants();      
        return serializer.toString();
    }

    public long count() throws SQLException {
        forCountRow = true;  
        String queryString = toString();
        logger.debug("query : {}", queryString);
        System.out.println(queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        ResultSet rs = null;
        try{
            int counter = 1;
            for (Object o : constants){
                stmt.setObject(counter++, o);    
            }        
            rs = stmt.executeQuery();   
            rs.next();
            long rv = rs.getLong(1);
            return rv;   
        }finally{
            try{
                if (rs != null) rs.close();    
            }finally{
                stmt.close();
            }            
        }        
    }

    public SqlQuery limit(int i) {
        this.limit = i;
        return this;
    }
    
    public SqlQuery offset(int o) {
        this.offset = o;
        return this;
    }
    
    public class UnionBuilder<RT>{
        
        public UnionBuilder<RT> orderBy(OrderSpecifier<?>... o) {
            SqlQuery.this.orderBy(o);
            return this;
        }
        
        public List<RT> list() throws SQLException {
            if (sq[0].getQuery().getMetadata().getSelect().size() == 1){
                return SqlQuery.this.listSingle(null);    
            }else{
                return (List<RT>) SqlQuery.this.listMultiple();
            }
            
        }
        
    }

}
