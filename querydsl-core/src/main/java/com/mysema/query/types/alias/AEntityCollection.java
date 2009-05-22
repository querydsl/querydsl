/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.alias;

import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.Path;

/**
 * Entity collection as alias
 */
public class AEntityCollection<D> extends EEntity<D> implements AToPath{
    private final Expr<?> from;
    private final Path<?> to;
    public AEntityCollection(PEntityCollection<D> from, Path<D> to) {
        super(null);
        this.from = from;
        this.to = to;
    }
    public Expr<?> getFrom() {return from;}
    public  Path<?> getTo() {return to;}        
}