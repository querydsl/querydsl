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
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ESimple;
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
    
    public EBoolean create(Boolean arg){
        try{
            return aliasFactory.isBound() ? aliasFactory.<PBoolean>getCurrent() : super.create(arg);    
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public PBooleanArray create(Boolean[] args){
        try{
            return aliasFactory.isBound() ? aliasFactory.<PBooleanArray>getCurrent() : super.create(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D> PComponentCollection<D> create(Collection<D> arg) {
        try{
            return aliasFactory.isBound() ? aliasFactory.<PComponentCollection<D>>getCurrent() : super.create(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D extends Comparable<D>> EComparable<D> create(D arg){
        try{
            return aliasFactory.isBound() ? aliasFactory.<EComparable<D>>getCurrent() : super.create(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    @SuppressWarnings("unchecked")
    public <D> ESimple<D> create(D arg){
        try{
            if (arg instanceof ManagedObject){
                PSimple<D> path = (PSimple<D>) aliasFactory.pathForAlias(arg);
                return path != null ? path : super.create(arg);    
            }else{
                return super.create(arg);
            }            
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D extends Comparable<D>> PComparableArray<D> create(D[] args){
        try{
            return aliasFactory.isBound() ? aliasFactory.<PComparableArray<D>>getCurrent() : super.create(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }

    public <D> PComponentList<D> create(List<D> arg) {
        try{
            return aliasFactory.isBound() ? aliasFactory.<PComponentList<D>>getCurrent() : super.create(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public ExtString create(String arg){
        try{
            return aliasFactory.isBound() ? aliasFactory.<ExtString>getCurrent() : super.create(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public PStringArray create(String[] args){
        try{
            return aliasFactory.isBound() ? aliasFactory.<PStringArray>getCurrent() : super.create(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }

}
