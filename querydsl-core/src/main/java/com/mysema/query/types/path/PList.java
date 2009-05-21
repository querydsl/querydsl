/**
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

public interface PList<D> extends PCollection<D> {
	Expr<D> get(Expr<Integer> index);

	Expr<D> get(int index);
}