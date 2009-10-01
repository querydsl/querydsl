package com.mysema.query.annotations;

/**
 * PropertyType defines the Path type to be used for a Domain property
 * 
 * @author tiwe
 *
 */
public enum PropertyType {
    /**
     *  for PDate fields
     */
    DATE,
    /**
     *  for PDateTime fields
     */
    DATETIME,
    /**
     *  to skip properties
     */
    NONE,
    /**
     * for PNumber fields
     */
    NUMERIC,
    /**
     * for PSimple fields
     */
    SIMPLE,
    /**
     * for PTime fields
     */
    TIME
    
    

}
