package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;

/**
 * Projectable provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Projectable {
    /**
     * return the amount of matched rows
     */
    long count();

    /**
     * iterate over the results with the given projection
     */
    Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest);

    /**
     * iterate over the results with the given projection
     */
    <RT> Iterator<RT> iterate(Expr<RT> projection);

    /**
     * list the results with the given projection
     */
    List<Object[]> list(Expr<?> e1, Expr<?> e2, Expr<?>... rest);

    /**
     * list the results with the given projection
     */
    <RT> List<RT> list(Expr<RT> projection);

    /**
     * return a unique result for the given projection or null for an empty result
     */
    <RT> RT uniqueResult(Expr<RT> expr);


}