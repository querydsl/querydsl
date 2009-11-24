/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.SQLException;
import java.util.List;

import com.mysema.query.types.OrderSpecifier;

/**
 * @author tiwe
 *
 * @param <RT>
 */
public interface Union<RT> {
    
    Union<RT> orderBy(OrderSpecifier<?>... o);

    List<RT> list() throws SQLException;

}
