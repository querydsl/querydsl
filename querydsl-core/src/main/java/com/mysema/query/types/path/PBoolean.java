/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 */
public class PBoolean extends EBoolean implements Path<Boolean> {
	private EBoolean isnull, isnotnull;
	private final PathMetadata<?> metadata;
	private final Path<?> root;

	public PBoolean(PathMetadata<?> metadata) {
		this.metadata = metadata;
		this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
	}

	public PBoolean(String var) {
		this(PathMetadata.forVariable(var));
	}

	public PathMetadata<?> getMetadata() {
		return metadata;
	}

	public EBoolean isnotnull() {
		if (isnotnull == null){
			isnotnull = Grammar.isnotnull(this);
		}			
		return isnotnull;
	}

	public EBoolean isnull() {
		if (isnull == null){
			isnull = Grammar.isnull(this);
		}			
		return isnull;
	}

	public Path<?> getRoot() {
		return root;
	}

	public int hashCode() {
		return metadata.hashCode();
	}

	public boolean equals(Object o) {
		return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
				: false;
	}
}