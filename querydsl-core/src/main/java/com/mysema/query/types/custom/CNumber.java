/**
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

public abstract class CNumber<T extends Number & Comparable<?>> extends
		ENumber<T> implements Custom<T> {
	public CNumber(Class<T> type) {
		super(type);
	}

	public Expr<?> getArg(int index) {
		return getArgs().get(index);
	}
}