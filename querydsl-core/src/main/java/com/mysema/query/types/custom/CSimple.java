/**
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;

public abstract class CSimple<T> extends ESimple<T> implements Custom<T> {
	public CSimple(Class<? extends T> type) {
		super(type);
	}

	public Expr<?> getArg(int index) {
		return getArgs().get(index);
	}
}