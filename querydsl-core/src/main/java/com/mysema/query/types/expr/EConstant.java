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
public class EConstant<D> extends Expr<D> {
	private final D constant;

	@SuppressWarnings("unchecked")
	public EConstant(D constant) {
		super((Class<D>) constant.getClass());
		this.constant = constant;
	}

	public D getConstant() {
		return constant;
	}

	public int hashCode() {
		return constant.hashCode();
	}

	public boolean equals(Object o) {
		return o instanceof EConstant ? ((EConstant<?>) o).getConstant()
				.equals(constant) : false;
	}
}