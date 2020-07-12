package com.querydsl.core.support;


import com.querydsl.core.ReactiveFetchable;
import com.querydsl.core.ReactiveFetchableQuery;
import com.querydsl.core.ReactiveResultTransformer;
import com.querydsl.core.types.SubQueryExpression;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * {@code FetchableQueryBase} extends the {@link QueryBase} class to provide default
 * implementations of the methods of the {@link com.querydsl.core.ReactiveFetchable} interface
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 */
public abstract class ReactiveFetchableQueryBase<T, Q extends ReactiveFetchableQueryBase<T, Q>>
        extends QueryBase<Q> implements ReactiveFetchable<T> {

    public ReactiveFetchableQueryBase(QueryMixin<Q> queryMixin) {
        super(queryMixin);
    }

    @Override
    public final Mono<T> fetchFirst() {
        return fetch().take(1).singleOrEmpty();
    }

    @Override
    public Mono<T> fetchOne() {
        return fetch().singleOrEmpty();
    }

    public <T> Publisher<T> transform(ReactiveResultTransformer<T> transformer) {
        return transformer.transform((ReactiveFetchableQuery<?, ?>) this);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SubQueryExpression) {
            SubQueryExpression<?> s = (SubQueryExpression<?>) o;
            return s.getMetadata().equals(queryMixin.getMetadata());
        } else {
            return false;
        }
    }

}

