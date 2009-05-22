/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

@SuppressWarnings("unchecked")
public class PComparableArray<D extends Comparable> extends PArray<D> {
	public PComparableArray(Class<D> type, PathMetadata<?> metadata) {
		super(type, metadata);
	}

	public PComparableArray(Class<D> type, String var) {
		super(type, PathMetadata.forVariable(var));
	}

	public EComparable<D> get(Expr<Integer> index) {
		return new PComparable<D>(componentType, PathMetadata.forArrayAccess(
				this, index));
	}

	public EComparable<D> get(int index) {
		return new PComparable<D>(componentType, PathMetadata.forArrayAccess(
				this, index));
	}
}
