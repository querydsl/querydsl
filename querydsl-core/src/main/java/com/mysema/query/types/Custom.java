/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.List;


/**
 * Custom provides base types for custom expressions with integrated
 * serialization templates
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Custom<T> {

    /** 
     * Get the argument with the given index
     * 
     * @param index
     * @return
     */
    Expr<?> getArg(int index);

    /**
     * Get the arguments of the custom expression
     * 
     * @return
     */
    List<Expr<?>> getArgs();

    /**
     * Get the serialization template for this custom expression
     * 
     * @return
     */
    Template getTemplate();
    
    /**
     * @return
     */
    Class<? extends T> getType();
    
    /**
     * Cast to {@link Expr}
     * 
     * @return
     */
    Expr<T> asExpr();

}
