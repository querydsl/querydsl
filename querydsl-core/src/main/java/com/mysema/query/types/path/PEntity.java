/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.alias.AEntity;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;

public class PEntity<D> extends EEntity<D> implements Path<D> {
	private EBoolean isnull, isnotnull;
	private final PathMetadata<?> metadata;
	private final String entityName;
	private final Path<?> root;

	public PEntity(Class<? extends D> type, String entityName, PathMetadata<?> metadata) {
		super(type);
		this.entityName = entityName;
		this.metadata = metadata;
		this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
	}

	public PEntity(Class<? extends D> type, String entityName, String var) {
		this(type, entityName, PathMetadata.forVariable(var));
	}

	protected PBoolean _boolean(String path) {
		return new PBoolean(PathMetadata.forProperty(this, path));
	}

	protected <A extends Comparable<?>> PComparable<A> _comparable(String path, Class<A> type) {
		return new PComparable<A>(type, PathMetadata.forProperty(this, path));
	}

	/**
	 * @param <A>
	 * @param path
	 * @param type
	 * @return
	 */
	protected <A extends Number & Comparable<?>> PNumber<A> _number(
			String path, Class<A> type) {
		return new PNumber<A>(type, PathMetadata.forProperty(this, path));
	}

	protected <A> PEntity<A> _entity(String path, String entityName,
			Class<A> type) {
		return new PEntity<A>(type, entityName, PathMetadata.forProperty(this,
				path));
	}

	protected <A> PEntityCollection<A> _entitycol(String path, Class<A> type,
			String entityName) {
		return new PEntityCollection<A>(type, entityName, PathMetadata
				.forProperty(this, path));
	}

	protected <A> PEntityList<A> _entitylist(String path, Class<A> type,
			String entityName) {
		return new PEntityList<A>(type, entityName, PathMetadata.forProperty(
				this, path));
	}

	protected <K, V> PEntityMap<K, V> _entitymap(String path, Class<K> key,
			Class<V> value, String entityName) {
		return new PEntityMap<K, V>(key, value, entityName, PathMetadata
				.forProperty(this, path));
	}

	protected <A> PSimple<A> _simple(String path, Class<A> type) {
		return new PSimple<A>(type, PathMetadata.forProperty(this, path));
	}

	protected <A> PComponentCollection<A> _simplecol(String path, Class<A> type) {
		return new PComponentCollection<A>(type, PathMetadata.forProperty(this,
				path));
	}

	protected <A> PComponentList<A> _simplelist(String path, Class<A> type) {
		return new PComponentList<A>(type, PathMetadata.forProperty(this, path));
	}

	protected <K, V> PComponentMap<K, V> _simplemap(String path, Class<K> key,
			Class<V> value) {
		return new PComponentMap<K, V>(key, value, PathMetadata.forProperty(
				this, path));
	}

	protected PString _string(String path) {
		return new PString(PathMetadata.forProperty(this, path));
	}

	public AEntity<D> as(PEntity<D> to) {
		return Grammar.as(this, to);
	}

	public String getEntityName() {
		return entityName;
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

	public <B extends D> EBoolean typeOf(Class<B> type) {
		return Grammar.typeOf(this, type);
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