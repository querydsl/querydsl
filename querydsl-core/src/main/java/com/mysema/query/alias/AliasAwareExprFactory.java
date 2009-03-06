/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;

import com.mysema.query.SimpleExprFactory;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
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
    
    @SuppressWarnings("unchecked")
    public <D> Expr<D> createAny(D arg){
        Expr<D> current = (Expr<D>)aliasFactory.getCurrentAndReset();
        if (current != null){
            return current;
        }else if (arg instanceof ManagedObject){
            return (Expr<D>)((ManagedObject)arg).__mappedPath();
        }else{
            throw new IllegalArgumentException("No path mapped to " + arg);
        }        
    }
    
    public EBoolean createBoolean(Boolean arg){
        EBoolean rv = aliasFactory.<PBoolean>getCurrentAndReset();
        return rv != null ? rv : super.createBoolean(arg);
    }
    
    public PBooleanArray createBooleanArray(Boolean[] args){
        PBooleanArray rv = aliasFactory.<PBooleanArray>getCurrentAndReset();
        return rv != null ? rv : super.createBooleanArray(args);
    }
    
    public <D> PEntityCollection<D> createEntityCollection(Collection<D> arg) {
        PEntityCollection<D> rv = aliasFactory.<PEntityCollection<D>>getCurrentAndReset();
        return rv != null ? rv : super.createEntityCollection(arg);
    }
    
    public <D extends Comparable<? super D>> EComparable<D> createComparable(D arg){
        EComparable<D> rv = aliasFactory.<EComparable<D>>getCurrentAndReset();
        return rv != null ? rv : super.createComparable(arg);
    }
    
    public <D extends Number & Comparable<? super D>> ENumber<D> createNumber(D arg){
        ENumber<D> rv = aliasFactory.<ENumber<D>>getCurrentAndReset();
        return rv != null ? rv : super.createNumber(arg);
    }
    
    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg){
        PEntity<D> rv = aliasFactory.<PEntity<D>>getCurrentAndReset();
        if (rv != null){
            return rv;
        }else if (arg instanceof ManagedObject){
            return (PEntity<D>)((ManagedObject)arg).__mappedPath(); 
        }else{
            return super.createEntity(arg);
        }                 
    }
    
    public <D extends Comparable<? super D>> PComparableArray<D> createComparableArray(D[] args){
        PComparableArray<D> rv = aliasFactory.<PComparableArray<D>>getCurrentAndReset();
        return rv != null ? rv : super.createComparableArray(args);
    }

    public <D> PEntityList<D> createEntityList(List<D> arg) {
        PEntityList<D> rv = aliasFactory.<PEntityList<D>>getCurrentAndReset();
        return rv != null ? rv : super.createEntityList(arg);
    }
    
    public ExtString createString(String arg){
        ExtString rv = aliasFactory.<ExtString>getCurrentAndReset();
        return rv != null ? rv : super.createString(arg);
    }
    
    public PStringArray createStringArray(String[] args){
        PStringArray rv = aliasFactory.<PStringArray>getCurrentAndReset();
        return rv != null ? rv : super.createStringArray(args);
    }

}
