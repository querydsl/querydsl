package com.mysema.query.collections.eval;

/**
 * Evaluator provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface Evaluator {

    <T> T evaluate(Object... args);
    
}
