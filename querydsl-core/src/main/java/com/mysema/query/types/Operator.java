/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

/**
 * Operator represents operator symbols
 *
 * @author tiwe
 *
 * @param <RT>
 */
public interface Operator<RT> {
    
    /**
     * Get the types related to this operator symbols
     *
     * @return
     */
    List<Class<?>> getTypes();

}
