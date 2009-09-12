/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PBooleanArray represents boolean array typed paths
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("serial")
public class PBooleanArray extends PArray<Boolean> {
    
    public PBooleanArray(PathMetadata<?> metadata) {
        super(Boolean.class, metadata);
    }

    public PBooleanArray(@NotEmpty String var) {
        super(Boolean.class, PathMetadata.forVariable(var));
    }

    @Override
    public EBoolean get(Expr<Integer> index) {
        return new PBoolean(PathMetadata.forArrayAccess(this, index));
    }

    @Override
    public EBoolean get(int index) {
        return new PBoolean(PathMetadata.forArrayAccess(this, index));
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public Expr<Boolean[]> asExpr() {
        return this;
    }
    
}