/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import com.mysema.query.Projectable;

/**
 * Query interface for JPQL queries
 *
 * @author tiwe
 *
 */
public interface JPQLQuery extends JPQLCommonQuery<JPQLQuery>, Projectable {

    /**
     * Add the "fetch" flag to the last defined join
     *
     * @return
     */
    JPQLQuery fetch();

    /**
      * Add the "fetch all properties" flag to the last defined join.
      * @return
       */
    JPQLQuery fetchAll();

}
