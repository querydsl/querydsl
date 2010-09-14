package com.mysema.query.types;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class MixinBase<T> implements Expression<T>{

    private static final long serialVersionUID = -8862014178653364345L;

    protected final Class<? extends T> type;
    
    @Nullable
    private volatile String toString;

    public MixinBase(Class<? extends T> type){
        this.type = type;
    }
    
    public final Class<? extends T> getType() {
        return type;
    }
    
    @Override
    public final String toString() {
        if (toString == null) {            
            toString = accept(ToStringVisitor.DEFAULT, Templates.DEFAULT);
        }
        return toString;
    }
    
}
