/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import com.mysema.query.QueryMetadata;

/**
 * @author tiwe
 *
 */
public interface SerializationContext {

    /**
     * @param metadata
     * @param forCountRow
     */
    void serialize(QueryMetadata metadata, boolean forCountRow);

    /**
     * @param str
     */
    SerializationContext append(String str);

    /**
     * @param string
     * @param offset
     */
    void handle(String template, Object... args);

}
