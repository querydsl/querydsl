/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.List;

import com.mysema.query.QueryBase;

/**
 * HqlQueryBase provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlQueryBase<A extends HqlQueryBase<A>> extends QueryBase<A>{
    
    private List<Object> constants;
    
    private String countRowsString, queryString;
    
    private String buildQueryString(boolean forCountRow) {
        if (joins.isEmpty()){
            throw new IllegalArgumentException("No where clause given");
        }
        HqlSerializer serializer = new HqlSerializer();
        serializer.serialize(select, joins, where.self(), groupBy, having.self(), orderBy, forCountRow);               
        constants = serializer.getConstants();      
        return serializer.toString();
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
