/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.dml;

import com.mysema.query.FilteredClause;


/**
 * DeleteClause defines a generic interface for Delete clauses
 *
 * @author tiwe
 *
 * @param <C>
 */
public interface DeleteClause<C extends DeleteClause<C>> extends DMLClause<C>, FilteredClause<C>{

}
