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
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * ExprFactory creates path expressions for domain type instances
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {

    EBoolean createBoolean(Boolean arg);

    PBooleanArray createBooleanArray(Boolean[] args);
    
    <D extends Number & Comparable<D>> ENumber<D> createNumber(D arg);

    <D extends Comparable<D>> EComparable<D> createComparable(D arg);

    <D> PEntity<D> createEntity(D arg);
    
    <D> PEntityList<D> createEntityList(List<D> arg);
    
    <D> PEntityCollection<D> createEntityCollection(Collection<D> arg);

    <D extends Comparable<D>> PComparableArray<D> createComparableArray(D[] args);

    ExtString createString(String arg);

    PStringArray createStringArray(String[] args);

}