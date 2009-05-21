/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.support.QueryBaseWithProjection;
import com.mysema.query.types.CascadingBoolean;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PathMetadata;

/**
 * HqlQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HQLQueryBase<SubType extends HQLQueryBase<SubType>> extends QueryBaseWithProjection<HQLJoinMeta,SubType>{
    
    private static final Logger logger = LoggerFactory
            .getLogger(HQLQueryBase.class);
        
    private List<Object> constants;
    
    private String countRowsString, queryString;
    
    private final HQLOps ops;
    
    public HQLQueryBase(HQLOps ops){
        this.ops = ops;
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No joins given");
        }
        HQLSerializer serializer = new HQLSerializer(ops);
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
    
    protected EBoolean createQBECondition(PEntity<?> entity,
            Map<String, Object> map) {
        CascadingBoolean expr = new CascadingBoolean();  
        for (Map.Entry<String, Object> entry : map.entrySet()){                
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            PSimple<Object> path = new PSimple<Object>(Object.class, md);
            if (entry.getValue() != null){
                expr.and(path.eq(entry.getValue()));
            }else{
                expr.and(path.isnull());                        
            }                    
        } 
        return expr.create();
    }
    
    public SubType forExample(PEntity<?> entity, Map<String, Object> map) {
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
    
    public SubType innerJoin(HQLJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HQLJoinMeta>(JoinType.INNERJOIN, o, meta));
        return _this;
    }
    
    public SubType fullJoin(HQLJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HQLJoinMeta>(JoinType.FULLJOIN, o, meta));
        return _this;
    }

    public SubType leftJoin(HQLJoinMeta meta, EEntity<?> o) {
        getMetadata().addJoin(new JoinExpression<HQLJoinMeta>(JoinType.LEFTJOIN, o, meta));
        return _this;
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
