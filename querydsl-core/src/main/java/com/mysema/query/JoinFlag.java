/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.custom.CString;

/**
 * @author tiwe
 *
 */
@Immutable
public class JoinFlag {
    
    private final Expr<?> flag;
    
    public JoinFlag(String flag) {
        this.flag = CString.create(flag);
    }
    
    public JoinFlag(Expr<?> flag) {
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

    public Expr<?> getFlag() {
        return flag;
    }

    
}
