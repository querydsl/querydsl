package com.querydsl.core.dml;

import reactor.core.publisher.Mono;

/**
 * Parent interface for DML clauses
 *
 * @param <C> concrete subtype
 */
public interface ReactiveDMLClause<C extends ReactiveDMLClause<C>> {

    /**
     * Execute the clause and return the amount of affected rows
     *
     * @return amount of affected rows or empty if not available
     */
    Mono<Long> execute();

}
