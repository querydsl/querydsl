/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * SQLSubQuery is a subquery implementation for SQL queries
 * 
 * @author tiwe
 *
 */
public class SQLSubQuery extends DetachableQuery<SQLSubQuery>{
    
    public SQLSubQuery() {
        this(new DefaultQueryMetadata());
    }
    
    public SQLSubQuery(QueryMetadata metadata) {
        super(new QueryMixin<SQLSubQuery>(metadata));
        this.queryMixin.setSelf(this);
    }
        
    public SQLSubQuery from(PEntity<?>... args){
        return queryMixin.from(args);
    }
    
    public SQLSubQuery fullJoin(PEntity<?> target) {
        return queryMixin.fullJoin(target);
    }
    
    public SQLSubQuery innerJoin(PEntity<?> target) {
        return queryMixin.innerJoin(target);
    }
    
    public SQLSubQuery join(PEntity<?> target) {
        return queryMixin.join(target);
    }
    
    public SQLSubQuery leftJoin(PEntity<?> target) {
        return queryMixin.leftJoin(target);
    }
    
    public SQLSubQuery on(EBoolean... conditions){
        return queryMixin.on(conditions);
    }
    
    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            SQLSerializer serializer = new SQLSerializer(SQLTemplates.DEFAULT);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }        
    }
}
