/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.Projectable;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.SqlJoinMeta;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.SqlSerializer;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.SubQuery;

/**
 * AbstractSqlQuery is the base type for SQL query implementations
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractSqlQuery<SubType extends AbstractSqlQuery<SubType>> extends QueryBase<SqlJoinMeta,SubType> implements Projectable{
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractSqlQuery.class);
    
    private String queryString;
    
    private int limit, offset;
    
    private List<Object> constants;
    
    private final Connection conn;
    
    protected final SqlOps ops;
    
    private boolean forCountRow = false;
    
    private SubQuery<SqlJoinMeta, ?>[] sq;
    
    @SuppressWarnings("unchecked")
    private SubType _this = (SubType)this;
    
    public AbstractSqlQuery(Connection conn, SqlOps ops){
        this.conn = conn;
        this.ops = ops;
    }
    
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        select(expr1, expr2);
        select(rest);
        try {
            return listMultiple();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private List<Object[]> listMultiple() throws SQLException{
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants){
            try {
                set(stmt, counter++, o);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(e.getMessage(), e);
            }
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
        
    public <RT> List<RT> list(Expr<RT> expr){
        select(expr);
        try {
            return listSingle(expr);
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
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
                throw new RuntimeException(e.getMessage(), e);
            }
        }        
        ResultSet rs = stmt.executeQuery();        
        try{
            List<RT> rv = new ArrayList<RT>();
            if (expr instanceof Expr.EConstructor){                
                Expr.EConstructor<RT> c = (Expr.EConstructor<RT>)expr;
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
                        throw new RuntimeException(e.getMessage(), e);
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

    protected SqlSerializer createSerializer(){
        return new SqlSerializer(ops);
    }
    
    protected String buildQueryString() {
        SqlSerializer serializer = createSerializer();
        if (sq != null){
            serializer.serializeUnion(select, sq, where.create(), orderBy);
        }else{
            serializer.serialize(select, joins, where.create(), groupBy, having.create(), orderBy, limit, offset, forCountRow);    
        }                       
        constants = serializer.getConstants();      
        return serializer.toString();
    }

    private long getCount() throws SQLException{
        forCountRow = true;  
        String queryString = toString();
        logger.debug("query : {}", queryString);
        System.out.println(queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        ResultSet rs = null;
        try{
            int counter = 1;
            for (Object o : constants){
                try {
                    set(stmt, counter++, o);
                } catch (Exception e) {
                    String error = "Caught " + e.getClass().getName();
                    logger.error(error, e);
                    throw new RuntimeException(e.getMessage(), e);
                }
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
    
    public long count(){
        try {
            return getCount();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public SubType limit(int i) {
        this.limit = i;
        return _this;
    }
    
    public SubType offset(int o) {
        this.offset = o;
        return _this;
    }
    
    public class UnionBuilder<RT>{
        
        public UnionBuilder<RT> orderBy(OrderSpecifier<?>... o) {
            AbstractSqlQuery.this.orderBy(o);
            return this;
        }
        
        public List<RT> list() throws SQLException {
            if (sq[0].getQuery().getMetadata().getSelect().size() == 1){
                return AbstractSqlQuery.this.listSingle(null);    
            }else{
                return (List<RT>) AbstractSqlQuery.this.listMultiple();
            }
            
        }
        
    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        List<RT> list = list(expr);
        return !list.isEmpty() ? list.get(0) : null;
    }

}
