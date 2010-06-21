/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.types.Ops;

/**
 * H2Templates is an SQL dialect for H2
 *
 * @author tiwe
 *
 */
public class H2Templates extends SQLTemplates{

    public H2Templates(){
        this(false);
    }

    public H2Templates(boolean quote){
        super(quote ? "\"" : null);
        add(Ops.MathOps.ROUND, "round({0},0)");
        add(Ops.TRIM, "trim(both from {0})");
        add(Ops.CONCAT, "concat({0},{1})");
    }

}
