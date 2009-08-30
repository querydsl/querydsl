/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PStringArray represents String[] typed path
 * 
 * @author tiwe
 * 
 */
public class PStringArray extends PArray<String> {
    
    public PStringArray(PathMetadata<?> metadata) {
        super(String.class, metadata);
    }

    public PStringArray(@NotEmpty String var) {
        super(String.class, PathMetadata.forVariable(var));
    }

    @Override
    public EString get(Expr<Integer> index) {
        return new PString(PathMetadata.forArrayAccess(this, index));
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public EString get(int index) {
        // TODO : cache
        return new PString(PathMetadata.forArrayAccess(this, index));
    }
}