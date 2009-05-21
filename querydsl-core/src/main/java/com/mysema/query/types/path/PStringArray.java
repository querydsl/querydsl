package com.mysema.query.types.path;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public class PStringArray extends PArray<String> {
	public PStringArray(PathMetadata<?> metadata) {
		super(String.class, metadata);
	}

	public PStringArray(String var) {
		super(String.class, PathMetadata.forVariable(var));
	}

	public EString get(Expr<Integer> index) {
		return new PString(PathMetadata.forArrayAccess(this, index));
	}

	public EString get(int index) {
		// TODO : cache
		return new PString(PathMetadata.forArrayAccess(this, index));
	}
}