/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;

/**
 * @author tiwe
 *
 */
object Constants {
    
    def constant[T](value: T) = new ConstantImpl(value);
    
}