/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import org.apache.commons.lang.ObjectUtils;

import net.jcip.annotations.Immutable;

/**
 * QueryModifiers combines limit and offset info into a single type.
 *
 * @author tiwe
 */
@Immutable
public final class QueryModifiers implements Serializable{

    private static final long serialVersionUID = 2934344588433680339L;

    public static QueryModifiers limit(@Nonnegative long limit) {
        return new QueryModifiers(Long.valueOf(limit), null);
    }

    public static QueryModifiers offset(@Nonnegative long offset) {
        return new QueryModifiers(null, Long.valueOf(offset));
    }


    @Nullable
    private final Long limit, offset;

    /**
     * Instantiates a new query modifiers.
     */
    public QueryModifiers() {
        limit = null;
        offset = null;
    }

    /**
     * Instantiates a new query modifiers.
     *
     * @param limit the limit
     * @param offset the offset
     */
    public QueryModifiers(@Nullable Long limit, @Nullable Long offset) {
        this.limit = limit;
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        this.offset = offset;
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative.");
        }
    }

    /**
     * @param modifiers
     */
    public QueryModifiers(QueryModifiers modifiers) {
        this.limit = modifiers.getLimit();
        this.offset = modifiers.getOffset();
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    @Nullable
    public Long getLimit() {
        return limit;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    @Nullable
    public Long getOffset() {
        return offset;
    }

    /**
     * Checks if is restricting.
     *
     * @return true, if is restricting
     */
    public boolean isRestricting() {
        return limit != null || offset != null;
    }
    
    /**
     * Get a sublist based on the restriction of limit and offset
     * 
     * @param <T>
     * @param list
     * @return
     */
    public <T> List<T> subList(List<T> list) {
        if (!list.isEmpty()){
            int from = offset != null ? offset.intValue() : 0;
            int to = limit != null ? (from + limit.intValue()) : list.size();
            return list.subList(from, Math.min(to,list.size()));    
        }else{
            return list;
        }        
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof QueryModifiers){
            QueryModifiers qm = (QueryModifiers)o;
            return ObjectUtils.equals(qm.getLimit(), limit) && ObjectUtils.equals(qm.getOffset(), offset);            
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        if (limit != null){
            return limit.hashCode();
        }else if (offset != null){
            return offset.hashCode();
        }else{
            return 0;
        }
    }


}
