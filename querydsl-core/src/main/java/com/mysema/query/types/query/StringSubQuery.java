/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * Single result subquery
 * 
 * @author tiwe
 */
public final class StringSubQuery extends EString implements SubQuery<String>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryMixin<String> subQueryMixin;
    
    public StringSubQuery(QueryMetadata md) {
        subQueryMixin = new SubQueryMixin<String>(md);
        subQueryMixin.setSelf(this);
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public boolean equals(Object o) {
       return subQueryMixin.equals(o);
    }
    
    @Override
    public EBoolean exists() {
        return subQueryMixin.exists();
    }
    
    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public int hashCode(){
        return subQueryMixin.hashCode();
    }
    
    @Override
    public EBoolean notExists() {
        return subQueryMixin.notExists();
    }

}
