/*
 * 
 */
package com.mysema.query.sql.support;

import java.util.List;

import com.mysema.commons.lang.Pair;

/**
 * Common interface for ForeignKeyData and InverseForeignKeyData
 * 
 * @author tiwe
 *
 */
public interface KeyData {

    String getName();

    String getTable();

    List<Pair<String, String>> getColumns();

}