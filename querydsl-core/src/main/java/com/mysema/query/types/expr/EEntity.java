/**
 * 
 */
package com.mysema.query.types.expr;

/**
 * 
 * @author tiwe
 *
 * @param <D>
 */
public abstract class EEntity<D> extends Expr<D> {
	public EEntity(Class<? extends D> type) {
		super(type);
	}
}