package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

public class PBooleanArray extends PArray<Boolean> {
	public PBooleanArray(PathMetadata<?> metadata) {
		super(Boolean.class, metadata);
	}

	public PBooleanArray(String var) {
		super(Boolean.class, PathMetadata.forVariable(var));
	}

	public EBoolean get(Expr<Integer> index) {
		return new PBoolean(PathMetadata.forArrayAccess(this, index));
	}

	public EBoolean get(int index) {
		return new PBoolean(PathMetadata.forArrayAccess(this, index));
	}

}