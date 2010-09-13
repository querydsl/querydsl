/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.io.Serializable;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.custom.StringTemplate;

/**
 * Defines a positioned flag in a query for customization of query serialization
 * 
 * @author tiwe
 *
 */
@Immutable
public class QueryFlag implements Serializable{
    
    private static final long serialVersionUID = -7131081607441961628L;

    public enum Position {        
        
        START,
        
        START_OVERRIDE,
        
        AFTER_SELECT,
        
        AFTER_PROJECTION,
        
        BEFORE_FILTERS,
        
        AFTER_FILTERS,
        
        BEFORE_GROUP_BY,
        
        AFTER_GROUP_BY,
        
        BEFORE_HAVING,
        
        AFTER_HAVING,
        
        BEFORE_ORDER,
        
        AFTER_ORDER,
        
        END        
        
    }
    
    private final Position position;
    
    private final Expression<?> flag;
    
    public QueryFlag(Position position, String flag) {
        this(position, StringTemplate.create(flag));
    }
    
    public QueryFlag(Position position, Expression<?> flag) {
        this.position = Assert.notNull(position,"position");
        this.flag = Assert.notNull(flag,"flag");        
    }

    public Position getPosition() {
        return position;
    }

    public Expression<?> getFlag() {
        return flag;
    }

    @Override
    public int hashCode() {
        return flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof QueryFlag){
            QueryFlag other = (QueryFlag)obj;
            return other.position.equals(position) && other.flag.equals(flag);
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return position + " : " + flag;
    }
}
