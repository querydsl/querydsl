/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;

/**
 * Path represents a path expression
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Path<C> {
	/**
	 * 
	 * @return
	 */
	PathMetadata<?> getMetadata();

	/**
	 * 
	 * @return
	 */
	Path<?> getRoot();

	/**
	 * 
	 * @return
	 */
	EBoolean isnotnull();

	/**
	 * 
	 * @return
	 */
	EBoolean isnull();

	/**
	 * 
	 * @return
	 */
	Class<? extends C> getType();

}
