/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;

import com.mysema.query.SimpleExprFactory;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * AliasAwareExprFactory extends the SimpleExprFactory to return thread bound alias expressions, when present
 *
 * @author tiwe
 * @version $Id$
 */
public class AliasAwareExprFactory extends SimpleExprFactory{
    
    private final AliasFactory aliasFactory;
    
    public AliasAwareExprFactory(AliasFactory aliasFactory){
        this.aliasFactory = aliasFactory;
    }
    
    public EBoolean createBoolean(Boolean arg){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PBoolean>getCurrent() : super.createBoolean(arg);    
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public PBooleanArray createBooleanArray(Boolean[] args){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PBooleanArray>getCurrent() : super.createBooleanArray(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D> PEntityCollection<D> createEntityCollection(Collection<D> arg) {
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PEntityCollection<D>>getCurrent() : super.createEntityCollection(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D extends Comparable<D>> EComparable<D> createComparable(D arg){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<EComparable<D>>getCurrent() : super.createComparable(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D extends Number & Comparable<D>> ENumber<D> createNumber(D arg){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<ENumber<D>>getCurrent() : super.createNumber(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg){
        try{
            if (arg instanceof ManagedObject){
                PEntity<D> path = (PEntity<D>) aliasFactory.pathForAlias(arg);
                return path != null ? path : super.createEntity(arg);    
            }else{
                return super.createEntity(arg);
            }            
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public <D extends Comparable<D>> PComparableArray<D> createComparableArray(D[] args){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PComparableArray<D>>getCurrent() : super.createComparableArray(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }

    public <D> PEntityList<D> createEntityList(List<D> arg) {
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PEntityList<D>>getCurrent() : super.createEntityList(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public ExtString createString(String arg){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<ExtString>getCurrent() : super.createString(arg);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }
    
    public PStringArray createStringArray(String[] args){
        try{
            return aliasFactory.hasCurrent() ? aliasFactory.<PStringArray>getCurrent() : super.createStringArray(args);
        }finally{
            aliasFactory.setCurrent(null);
        }        
    }

}
