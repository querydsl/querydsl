package com.querydsl.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * {@code ReactiveFetchable} defines default projection methods for {@link Query} implementations.
 * All Querydsl query implementations should implement this interface.
 *
 * @param <T> result type
 */
public interface ReactiveFetchable<T> {

    /**
     * Get the projection as a typed Flux.
     *
     * @return result
     */
    Flux<T> fetch();

    /**
     * Get the first result of the projection.
     *
     * @return first result
     */
    Mono<T> fetchFirst();

    /**
     * Get the projection as a unique result.
     *
     * @return first result
     */
    Mono<T> fetchOne();

    /**
     * Get the count of matched elements
     *
     * @return row count
     */
    Mono<Long> fetchCount();

}
