package com.mysema.query.types;


/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class MixinBase<T> implements Expression<T>{

    private static final long serialVersionUID = 3368323881036494054L;

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Class<? extends T> getType() {
        throw new UnsupportedOperationException();
    }
    
}
