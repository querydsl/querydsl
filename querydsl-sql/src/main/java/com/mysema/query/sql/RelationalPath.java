/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;

/**
 * RelationalPath extends EntityPath to provide access to relational metadata
 * 
 * @author tiwe
 *
 */
public interface RelationalPath<T> extends EntityPath<T>{
    
    /**
     * Get the table name
     * 
     * @return
     */
    String getTableName();
    
    /**
     * Get all columns
     * 
     * @return
     */
    List<Path<?>> getColumns();

    /**
     * Get the primary key for this relation or null if none exists
     * 
     * @return
     */
    @Nullable
    PrimaryKey<T> getPrimaryKey();

    /**
     * Get the foreign keys for this relation
     * 
     * @return
     */
    Collection<ForeignKey<?>> getForeignKeys();

    /**
     * Get the inverse foreign keys for this relation
     * 
     * @return
     */
    Collection<ForeignKey<?>> getInverseForeignKeys();

}