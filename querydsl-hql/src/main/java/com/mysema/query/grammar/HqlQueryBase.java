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
import com.mysema.query.QueryBaseWithProjection;
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
public abstract class HqlQueryBase<SubType extends HqlQueryBase<SubType>> extends QueryBaseWithProjection<HqlJoinMeta,SubType>{
    
    private static final Logger logger = LoggerFactory
            .getLogger(HqlQueryBase.class);
        
    private List<Object> constants;
    
    private String countRowsString, queryString;
    
    protected Integer limit, offset;
    
    private final HqlOps ops;
    
    @SuppressWarnings("unchecked")
    private SubType _this = (SubType)this;
    
    public HqlQueryBase(HqlOps ops){
        this.ops = ops;
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No joins given");
        }
        HqlSerializer serializer = new HqlSerializer(ops);
        serializer.serialize(getMetadata(), forCountRow);               
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
        return expr.create();
    }
    
    public SubType forExample(Path.PEntity<?> entity, Map<String, Object> map) {
        addToProjection(entity).from(entity);
        try {            
            where(createQBECondition(entity,map));
            return _this;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    protected List<Object> getConstants() {
        return constants;
    }
    
    public SubType innerJoin(HqlJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HqlJoinMeta>(JoinType.INNERJOIN, o, meta));
        return _this;
    }
    
    public SubType fullJoin(HqlJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HqlJoinMeta>(JoinType.FULLJOIN, o, meta));
        return _this;
    }

    public SubType leftJoin(HqlJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HqlJoinMeta>(JoinType.LEFTJOIN, o, meta));
        return _this;
    }
    
    public SubType limit(int limit) {
        this.limit = limit;
        return _this;
    }
    
    public SubType offset(int offset) {
        this.offset = offset;
        return _this;
    }
    
    public SubType restrict(QueryModifiers mod) {
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
