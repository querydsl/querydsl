/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.Path;

/**
 * MiniApi provides static convenience methods for query construction
 *
 * @author tiwe
 */
public final class MiniApi {

    public static <A> ColDeleteClause<A> delete(Path<A> path, Collection<A> col) {
        return new ColDeleteClause<A>(path, col);
    }

    public static <A> ColQuery from(A alias, Iterable<A> col) {
        return new ColQueryImpl().from(Alias.$(alias), col);
    }

    public static <A> ColQuery from(Path<A> path, A... arr) {
        return new ColQueryImpl().from(path, Arrays.asList(arr));
    }

    public static <A> ColQuery from(Path<A> path, Iterable<A> col) {
        return new ColQueryImpl().from(path, col);
    }

    public static <A> ColUpdateClause<A> update(Path<A> path, Iterable<A> col) {
        return new ColUpdateClause<A>(path, col);
    }

    private MiniApi() {}

}
