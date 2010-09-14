package com.mysema.query.types;

/**
 * @author tiwe
 *
 */
public interface Predicate extends Expression<Boolean>{
    
    /**
     * @return
     */
    Predicate not();

}
