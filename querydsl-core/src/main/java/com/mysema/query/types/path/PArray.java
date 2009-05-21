package com.mysema.query.types.path;

import java.lang.reflect.Array;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

public abstract class PArray<D> extends Expr<D[]> implements Path<D[]>,
		CollectionType<D> {
	protected final Class<D[]> arrayType;
	protected final Class<D> componentType;
	private EBoolean isnull, isnotnull;
	private final PathMetadata<?> metadata;
	private EComparable<Integer> size;
	private final Path<?> root;

	@SuppressWarnings("unchecked")
	public PArray(Class<D> type, PathMetadata<?> metadata) {
		super(null);
		this.arrayType = (Class<D[]>) Array.newInstance(type, 0).getClass();
		this.componentType = type;
		this.metadata = metadata;
		this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
	}

	public PArray(Class<D> type, String var) {
		this(type, PathMetadata.forVariable(var));
	}

	public abstract Expr<D> get(Expr<Integer> index);

	public abstract Expr<D> get(int index);

	public Class<D> getElementType() {
		return componentType;
	}

	public PathMetadata<?> getMetadata() {
		return metadata;
	}

	public Class<D[]> getType() {
		return arrayType;
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

	public EComparable<Integer> size() {
		if (size == null)
			size = new PComparable<Integer>(Integer.class, PathMetadata
					.forSize(this));
		return size;
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