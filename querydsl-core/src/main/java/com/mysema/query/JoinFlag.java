/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.io.Serializable;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.TemplateExpressionImpl;

/**
 * JoinFlag defines a join related flag using an Expression instance
 * 
 * @author tiwe
 *
 */
@Immutable
public class JoinFlag implements Serializable{
    
    public enum Position {
        
        /**
         * before the join
         */
        START,  
        
        /**
         * as a replacement for the join symbol
         */
        OVERRIDE,
        
        /**
         * before the join target
         */
        BEFORE_TARGET, 

        /**
         * before the join condition
         */
        BEFORE_CONDITION,
        
        /**
         * after the join 
         */
        END
        
    }
    
    private static final long serialVersionUID = -688265393547206465L;
    
    private final Expression<?> flag;
    
    private final Position position;
    
    public JoinFlag(String flag) {
        this(TemplateExpressionImpl.create(Object.class, flag), Position.BEFORE_TARGET);
    }
    
    public JoinFlag(String flag, Position position) {
        this(TemplateExpressionImpl.create(Object.class, flag), position);
    }
    
    
    public JoinFlag(Expression<?> flag) {
        this(flag, Position.BEFORE_TARGET);
    }
    
    public JoinFlag(Expression<?> flag, Position position) {
        this.flag = flag;
        this.position = position;
    }
    
    @Override
    public int hashCode() {
        return flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof JoinFlag) {
            return ((JoinFlag)obj).flag.equals(flag);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return flag.toString();
    }

    public Expression<?> getFlag() {
        return flag;
    }

    public Position getPosition() {
        return position;
    }
    
}
