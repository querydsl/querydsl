/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.alias;

import java.util.Collection;
import java.util.List;

import com.mysema.query.collections.SimpleExprFactory;
import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * AliasAwareExprFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasAwareExprFactory extends SimpleExprFactory{
    
    private final AliasFactory aliasFactory;
    
    public AliasAwareExprFactory(AliasFactory aliasFactory){
        this.aliasFactory = aliasFactory;
    }
    
    public PBoolean create(Boolean arg){
        return aliasFactory.isBound() ? aliasFactory.<PBoolean>getCurrent() : super.create(arg);
    }
    
    public PBooleanArray create(Boolean[] args){
        return aliasFactory.isBound() ? aliasFactory.<PBooleanArray>getCurrent() : super.create(args);
    }
    
    public <D> PComponentCollection<D> create(Collection<D> arg) {
        return aliasFactory.isBound() ? aliasFactory.<PComponentCollection<D>>getCurrent() : super.create(arg);
    }
    
    public <D extends Comparable<D>> PComparable<D> create(D arg){
        return aliasFactory.isBound() ? aliasFactory.<PComparable<D>>getCurrent() : super.create(arg);
    }
    
    @SuppressWarnings("unchecked")
    public <D> PSimple<D> create(D arg){
        PSimple<D> path = (PSimple<D>) aliasFactory.pathForAlias(arg);
        return path != null ? path : super.create(arg);        
    }
    
    public <D extends Comparable<D>> PComparableArray<D> create(D[] args){
        return aliasFactory.isBound() ? aliasFactory.<PComparableArray<D>>getCurrent() : super.create(args);
    }

    public <D> PComponentList<D> create(List<D> arg) {
        return aliasFactory.isBound() ? aliasFactory.<PComponentList<D>>getCurrent() : super.create(arg);
    }
    
    public ExtString create(String arg){
        return aliasFactory.isBound() ? aliasFactory.<ExtString>getCurrent() : super.create(arg);
    }
    
    public PStringArray create(String[] args){
        return aliasFactory.isBound() ? aliasFactory.<PStringArray>getCurrent() : super.create(args);
    }

}
