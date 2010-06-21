/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public interface SerializerConfig {

    /**
     * @return
     */
    boolean useEntityAccessors();

    /**
     * @return
     */
    boolean useListAccessors();

    /**
     * @return
     */
    boolean useMapAccessors();

    /**
     * @return
     */
    boolean createDefaultVariable();

}
