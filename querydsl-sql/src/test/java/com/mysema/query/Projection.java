/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;

public interface Projection {
    
    @Nullable
    <T> T get(Expr<T> expr); 

    @Nullable
    <T> T get(int index, Class<T> type); 

    @Nullable
    <T> Expr<T> getExpr(Expr<T> expr);
    
    @Nullable
    <T> Expr<T> getExpr(int index, Class<T> type);
    
    Object[] toArray();

}
