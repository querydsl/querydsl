/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.types.Expr.Entity;

/**
 * HqlQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlQueryBase<A extends HqlQueryBase<A>> extends QueryBase<JoinMeta,A>{
    
    private static final HqlOps OPS_DEFAULT = new HqlOps();
    
    private final HqlOps ops;
    
    private List<Object> constants;
    
    private String countRowsString, queryString;
    
    public HqlQueryBase(HqlOps ops){
        this.ops = ops;
    }
    
    public HqlQueryBase(){
        this.ops = OPS_DEFAULT;
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (joins.isEmpty()){
            throw new IllegalArgumentException("No where clause given");
        }
        HqlSerializer serializer = new HqlSerializer(ops);
        serializer.serialize(select, joins, where.self(), groupBy, having.self(), orderBy, forCountRow);               
        constants = serializer.getConstants();      
        return serializer.toString();
    }
    
    public A innerJoin(JoinMeta meta, Entity<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.INNERJOIN, o, meta));
        return (A) this;
    }
    
    public A leftJoin(JoinMeta meta, Entity<?> o) {
        joins.add(new JoinExpression<JoinMeta>(JoinType.LEFTJOIN, o, meta));
        return (A) this;
    }
    
    public List<Object> getConstants() {
        return constants;
    }

    @Override
    protected void clear(){
        super.clear();
        queryString = null;
        countRowsString = null;
    }
    
    @Override
    public String toString(){
        if (queryString == null){
            queryString = buildQueryString(false);    
        }        
        return queryString;
    }
    
    public String toCountRowsString(){
        if (countRowsString == null){
            countRowsString = buildQueryString(true);    
        }        
        return countRowsString;
    }

}
