package com.mysema.query;


public interface ResultTransformer<T> {

    public T transform(Projectable projectable);
    
}
