/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.util.Iterator;

/**
 * IteratorBase is an abstract base class for iterators
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class IteratorBase<RT> implements Iterator<RT>{
    public void remove() {
    }
}