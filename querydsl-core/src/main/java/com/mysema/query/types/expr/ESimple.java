/**
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;

/**
 * 
 * @author tiwe
 * 
 * @param <D>
 */
public abstract class ESimple<D> extends Expr<D> {
	public ESimple(Class<? extends D> type) {
		super(type);
	}

	public final Expr<D> as(String to) {
		return Grammar.as(this, to);
	}
}