/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.CascadingBoolean;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Expr.EEntity;
import com.mysema.query.hql.QueryModifiers;

/**
 * HqlQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HqlQueryBase<A extends HqlQueryBase<A>> extends QueryBase<HqlJoinMeta,A>{
    
    private static final Logger logger = LoggerFactory
            .getLogger(HqlQueryBase.class);
        
    private List<Object> constants;
    
    private String countRowsString, queryString;
    
    protected Integer limit, offset;
    
    private final HqlOps ops;
    
    public HqlQueryBase(HqlOps ops){
        this.ops = ops;
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (joins.isEmpty()){
            throw new IllegalArgumentException("No joins given");
        }
        HqlSerializer serializer = new HqlSerializer(ops);
        serializer.serialize(select, joins, where.self(), groupBy, having.self(), orderBy, forCountRow);               
        constants = serializer.getConstants();      
        return serializer.toString();
    }
    
    @Override
    protected void clear(){
        super.clear();
        queryString = null;
        countRowsString = null;
    }
    
    protected Expr.EBoolean createQBECondition(Path.PEntity<?> entity,
            Map<String, Object> map) {
        CascadingBoolean expr = new CascadingBoolean();  
        for (Map.Entry<String, Object> entry : map.entrySet()){                
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            Path.PSimple<Object> path = new Path.PSimple<Object>(Object.class, md);
            if (entry.getValue() != null){
                expr.and(path.eq(entry.getValue()));
            }else{
                expr.and(path.isnull());                        
            }                    
        } 
        return expr.self();
    }
    
    @SuppressWarnings("unchecked")
    public A forExample(Path.PEntity<?> entity, Map<String, Object> map) {
        select(entity).from(entity);
        try {            
            where(createQBECondition(entity,map));
            return (A)this;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }
    
    public List<Object> getConstants() {
        return constants;
    }
    
    @SuppressWarnings("unchecked")
    public A innerJoin(HqlJoinMeta meta, EEntity<?> o) {
        joins.add(new JoinExpression<HqlJoinMeta>(JoinType.INNERJOIN, o, meta));
        return (A) this;
    }
    
    public A fullJoin(EEntity<?> o) {
        // ?!?
        joins.add(new JoinExpression<HqlJoinMeta>(JoinType.INNERJOIN,o));
        return (A) this;
    }

    @SuppressWarnings("unchecked")
    public A leftJoin(HqlJoinMeta meta, EEntity<?> o) {
        joins.add(new JoinExpression<HqlJoinMeta>(JoinType.LEFTJOIN, o, meta));
        return (A) this;
    }
    
    @SuppressWarnings("unchecked")
    public A limit(int limit) {
        this.limit = limit;
        return (A)this;
    }
    
    @SuppressWarnings("unchecked")
    public A offset(int offset) {
        this.offset = offset;
        return (A)this;
    }
    
    public A restrict(QueryModifiers mod) {
        return limit(mod.getLimit()).offset(mod.getOffset());
    }    

    public String toCountRowsString(){
        if (countRowsString == null){
            countRowsString = buildQueryString(true);    
        }        
        return countRowsString;
    }

    @Override
    public String toString(){
        if (queryString == null){
            queryString = buildQueryString(false);    
        }        
        return queryString;
    }

}
