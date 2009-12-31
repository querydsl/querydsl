/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Query;

/**
 * @author tiwe
 *
 */
public interface SessionHolder {

    Query createQuery(String queryString);

}
