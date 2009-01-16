/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collection;
import java.util.List;

import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * ExprFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {

    EBoolean create(Boolean arg);

    PBooleanArray create(Boolean[] args);

    <D extends Comparable<D>> EComparable<D> create(D arg);

    <D> PEntity<D> create(D arg);
    
    <D> PEntityList<D> create(List<D> arg);
    
    <D> PEntityCollection<D> create(Collection<D> arg);

    <D extends Comparable<D>> PComparableArray<D> create(D[] args);

    ExtString create(String arg);

    PStringArray create(String[] args);

}