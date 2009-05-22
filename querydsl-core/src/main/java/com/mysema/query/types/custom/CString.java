/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public abstract class CString extends EString implements Custom<String> {
	public Expr<?> getArg(int index) {
		return getArgs().get(index);
	}
}