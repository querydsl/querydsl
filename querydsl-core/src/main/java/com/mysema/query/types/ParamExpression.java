/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * ParamExpression defines named and unnamed parameters in queries
 * 
 * @author tiwe
 *
 * @param <T> type of expression
 */
public interface ParamExpression<T> extends Expression<T>{
    
    /**
     * Get the name of the parameter
     * 
     * @return
     */
    String getName();

    /**
     * @return
     */
    boolean isAnon();

    /**
     * @return
     */
    String getNotSetMessage();

}
