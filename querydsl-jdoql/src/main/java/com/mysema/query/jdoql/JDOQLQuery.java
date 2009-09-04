/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.Closeable;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.path.PEntity;

/**
 * Query interface for JDOQL queries
 * 
 * @author tiwe
 * 
 */
public interface JDOQLQuery extends Query<JDOQLQuery>, Projectable, Closeable {

    JDOQLQuery from(PEntity<?>... sources);

}
