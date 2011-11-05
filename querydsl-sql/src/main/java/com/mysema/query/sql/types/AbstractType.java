package com.mysema.query.sql.types;


public abstract class AbstractType<T> implements Type<T> {
    
    private final int type;
    
    public AbstractType(int type) {
        this.type = type;
    }
    
    @Override
    public final int[] getSQLTypes() {
        return new int[]{type};
    }

}
