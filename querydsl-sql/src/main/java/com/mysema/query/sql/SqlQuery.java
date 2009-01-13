/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryBase;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.SqlSerializer;
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
    
    private final SqlOps ops;
    
    public SqlQuery(SqlOps ops){
        this.ops = ops;
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        select(expr1, expr2);
        select(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
//        Query query = createQuery(queryString, limit, offset);
//        return query.list();
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr){
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
//        Query query = createQuery(queryString, limit, offset);
//        return query.list();
        return null;
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
