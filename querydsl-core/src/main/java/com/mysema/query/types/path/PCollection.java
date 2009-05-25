/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <D>
 */
public interface PCollection<D> extends Path<java.util.Collection<D>>, CollectionType<D> {
	Class<D> getElementType();

	EBoolean contains(D child);

	EBoolean contains(Expr<D> child);

	EComparable<Integer> size();
	
	EBoolean empty();
	
	EBoolean notEmpty();
}