/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import com.mysema.query.serialization.ToStringVisitor;
import com.mysema.query.types.CollectionType;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.ValidationVisitor;



/**
 * Expr represents a general typed expression in a Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D>{
   
    static final SimpleExprFactory factory = new SimpleExprFactory();
    
    private final Class<? extends D> type;
    private String toString;
    
    public Expr(Class<? extends D> type){this.type = type;}
    public Class<? extends D> getType(){ return type;}
    
    // eq
    public final EBoolean eq(D right){return Grammar.eq(this, right);}        
    public final EBoolean eq(Expr<? super D> right){return Grammar.eq(this, right);}
    
    // ne
    public final EBoolean ne(D right){return Grammar.ne(this, right);}
    public final EBoolean ne(Expr<? super D> right){return Grammar.ne(this, right);}
    
    // containment
    public final EBoolean in(CollectionType<? extends D> arg) {return Grammar.in(this, arg);}
    public final EBoolean in(Collection<? extends D> arg) {return Grammar.in(this, arg); }
    public final EBoolean in(D... args) {return Grammar.in(this,args);}
    
    public final EBoolean notIn(CollectionType<? extends D> arg) {return Grammar.notIn(this, arg);}
    public final EBoolean notIn(Collection<? extends D> arg) {return Grammar.in(this, arg); }
    public final EBoolean notIn(D...args) {return Grammar.notIn(this, args);}
    
    protected void validate(){
        new ValidationVisitor().handle(this);
    }
    
    public String toString(){
        if (toString == null){
            toString = new ToStringVisitor().handle(this).toString();
        }
        return toString;
    }
    
    public int hashCode(){
        return type != null ? type.hashCode() : super.hashCode();
    }
}
