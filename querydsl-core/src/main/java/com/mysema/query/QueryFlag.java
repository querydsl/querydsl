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
import com.mysema.query.types.TemplateExpressionImpl;

/**
 * Defines a positioned flag in a Query for customization of query serialization
 * 
 * @author tiwe
 *
 */
@Immutable
public class QueryFlag implements Serializable{
    
    private static final long serialVersionUID = -7131081607441961628L;

    public enum Position {        
        
        /**
         * Start of the query 
         */
        START,
        
        /**
         * Override for the first element (e.g SELECT, INSERT)
         */
        START_OVERRIDE,
        
        /**
         * After the first element (after select)
         */
        AFTER_SELECT,
        
        /**
         * After the projection (after select ...)
         */
        AFTER_PROJECTION,
        
        /**
         * Before the filter conditions (where)
         */
        BEFORE_FILTERS,
        
        /**
         * After the filter conditions (where)
         */
        AFTER_FILTERS,
        
        /**
         * Before group by
         */
        BEFORE_GROUP_BY,
        
        /**
         * After group by 
         */
        AFTER_GROUP_BY,
        
        /**
         * Before having
         */
        BEFORE_HAVING,
        
        /**
         * After having
         */
        AFTER_HAVING,
        
        /**
         * Before order (by)
         */
        BEFORE_ORDER,
        
        /**
         * After order (by)
         */
        AFTER_ORDER,
        
        /**
         * After all other tokens 
         */
        END        
        
    }
    
    private final Position position;
    
    private final Expression<?> flag;
    
    public QueryFlag(Position position, String flag) {
        this(position, TemplateExpressionImpl.create(Object.class, flag));
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
        if (obj == this) {
            return true;
        } else if (obj instanceof QueryFlag) {
            QueryFlag other = (QueryFlag)obj;
            return other.position.equals(position) && other.flag.equals(flag);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return position + " : " + flag;
    }
}
