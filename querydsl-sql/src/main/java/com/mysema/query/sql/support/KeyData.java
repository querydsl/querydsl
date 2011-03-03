/*
 * 
 */
package com.mysema.query.sql.support;

import java.util.List;

import com.mysema.codegen.model.Type;

/**
 * Common interface for ForeignKeyData and InverseForeignKeyData
 * 
 * @author tiwe
 *
 */
public interface KeyData {

    String getName();

    String getTable();
    
    Type getType();
    
    List<String> getForeignColumns();

    List<String> getParentColumns();

}