/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import com.mysema.query.Projectable;

/**
 * Query interface for HQL queries
 *
 * @author tiwe
 *
 */
public interface HQLQuery extends HQLCommonQuery<HQLQuery>, Projectable {

    /**
     * Add the "fetch" flag to the last defined join
     *
     * @return
     */
    HQLQuery fetch();

    /**
      * Add the "fetch all properties" flag to the last defined join.
      * @return
       */
    HQLQuery fetchAll();

}
