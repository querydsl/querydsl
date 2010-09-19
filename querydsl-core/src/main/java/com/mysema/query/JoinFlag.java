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
 * JoinFlag defines a join related flag using an Expr instance
 * 
 * @author tiwe
 *
 */
@Immutable
public class JoinFlag implements Serializable{
    
    private static final long serialVersionUID = -688265393547206465L;
    
    private final Expression<?> flag;
    
    public JoinFlag(String flag) {
        this.flag = TemplateExpressionImpl.create(flag);
    }
    
    public JoinFlag(Expression<?> flag) {
        this.flag = flag;
    }
    
    @Override
    public int hashCode() {
        return flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof JoinFlag){
            return ((JoinFlag)obj).flag.equals(flag);
        }else{
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

    
}
