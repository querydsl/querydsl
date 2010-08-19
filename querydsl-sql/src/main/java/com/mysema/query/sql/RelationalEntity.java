package com.mysema.query.sql;

import java.util.Set;

import com.mysema.query.types.EntityPath;

/**
 * @author tiwe
 *
 */
interface RelationalEntity<T> extends EntityPath<T>{

    /**
     * @return
     */
    PrimaryKey<?> getPrimaryKey();

    /**
     * @return
     */
    Set<ForeignKey<?>> getForeignKeys();

    /**
     * @return
     */
    Set<ForeignKey<?>> getInverseForeignKeys();

}