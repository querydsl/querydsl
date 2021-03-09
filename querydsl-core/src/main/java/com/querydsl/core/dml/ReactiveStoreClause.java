package com.querydsl.core.dml;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;

import org.jetbrains.annotations.Nullable;

/**
 * Parent interface for {@link ReactiveInsertClause} and {@link ReactiveUpdateClause}
 *
 * @param <C> concrete subtype
 */
public interface ReactiveStoreClause<C extends ReactiveStoreClause<C>> extends ReactiveDMLClause<C> {

    /**
     * Add a value binding
     *
     * @param <T>
     * @param path  path to be updated
     * @param value value to set
     * @return the current object
     */
    <T> C set(Path<T> path, @Nullable T value);

    /**
     * Add an expression binding
     *
     * @param <T>
     * @param path       path to be updated
     * @param expression binding
     * @return the current object
     */
    <T> C set(Path<T> path, Expression<? extends T> expression);

    /**
     * Bind the given path to null
     *
     * @param path path to be updated
     * @return the current object
     */
    <T> C setNull(Path<T> path);

    /**
     * Returns true, if no bindings have been set, otherwise false.
     *
     * @return true, if empty, false, if not
     */
    boolean isEmpty();

}
