/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * Constant represents a general constant expression. 
 * EConst is default implementation class.
 * 
 * @author tiwe
 *
 * @param <D>
 */
public interface Constant<D> {
    
    /**
     * @return
     */
    D getConstant();

}
