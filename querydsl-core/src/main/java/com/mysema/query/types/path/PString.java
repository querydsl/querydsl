/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EString;

public class PString extends EString implements Path<String> {
	private EBoolean isnull, isnotnull;
	private final PathMetadata<?> metadata;

	public PString(PathMetadata<?> metadata) {
		this.metadata = metadata;
	}

	public PString(String var) {
		this(PathMetadata.forVariable(var));
	}

	public PathMetadata<?> getMetadata() {
		return metadata;
	}

	public EBoolean isnotnull() {
		if (isnotnull == null)
			isnotnull = Grammar.isnotnull(this);
		return isnotnull;
	}

	public EBoolean isnull() {
		if (isnull == null)
			isnull = Grammar.isnull(this);
		return isnull;
	}

	public Path<?> getRoot() {
		return metadata.getRoot() != null ? metadata.getRoot() : this;
	}

	public int hashCode() {
		return metadata.hashCode();
	}

	public boolean equals(Object o) {
		return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
				: false;
	}
}