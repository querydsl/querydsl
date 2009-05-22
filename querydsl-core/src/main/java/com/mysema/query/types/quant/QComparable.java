/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * The Class Comparable.
 */
public class QComparable<Q extends Comparable<? super Q>> extends
		EComparable<Q> implements Quant {
	private final Expr<?> col;
	private final Op<?> op;

	public QComparable(Class<Q> type, Op<?> op, CollectionType<Q> col) {
		super(type);
		this.op = op;
		this.col = (Expr<?>) col;
	}

	public Op<?> getOperator() {
		return op;
	}

	public Expr<?> getTarget() {
		return col;
	}
}