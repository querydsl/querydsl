/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

/**
 * CollectionType is a super type for array, collection and subquery expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public interface CollectionType<D>{
	/**
	 * 
	 * @return
	 */
    Class<D> getElementType();
}