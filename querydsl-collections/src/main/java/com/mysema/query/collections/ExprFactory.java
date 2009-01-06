/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collection;
import java.util.List;

import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.Path.PBooleanArray;
import com.mysema.query.grammar.types.Path.PComparableArray;
import com.mysema.query.grammar.types.Path.PComponentCollection;
import com.mysema.query.grammar.types.Path.PComponentList;
import com.mysema.query.grammar.types.Path.PStringArray;

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

    <D> ESimple<D> create(D arg);
    
    <D> PComponentList<D> create(List<D> arg);
    
    <D> PComponentCollection<D> create(Collection<D> arg);

    <D extends Comparable<D>> PComparableArray<D> create(D[] args);

    ExtString create(String arg);

    PStringArray create(String[] args);

}