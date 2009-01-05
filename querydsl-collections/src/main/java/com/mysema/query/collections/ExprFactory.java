/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collection;
import java.util.List;

import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * ExprFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {

    PBoolean create(Boolean arg);

    PBooleanArray create(Boolean[] args);

    <D extends Comparable<D>> PComparable<D> create(D arg);

    <D> PSimple<D> create(D arg);
    
    <D> PComponentList<D> create(List<D> arg);
    
    <D> PComponentCollection<D> create(Collection<D> arg);

    <D extends Comparable<D>> PComparableArray<D> create(D[] args);

    ExtString create(String arg);

    PStringArray create(String[] args);

}