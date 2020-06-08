package com.querydsl.core.dml;

import com.querydsl.core.types.Predicate;

/**
 * {@code ReactiveFilteredClause} is an interface for clauses with a filter condition
 *
 * @param <C> concrete subtype
 */
public interface ReactiveFilteredClause<C extends ReactiveFilteredClause<C>> {

    /**
     * Adds the given filter conditions
     *
     * <p>Skips null arguments</p>
     *
     * @param o filter conditions to be added
     * @return the current object
     */
    C where(Predicate... o);

}
