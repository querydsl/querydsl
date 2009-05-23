/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * Quant provides expressions for quantification.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Quant<T> {

	/**
	 * 
	 * @return
	 */
	Op<?> getOperator();

	/**
	 * 
	 * @return
	 */
	Expr<?> getTarget();

}
