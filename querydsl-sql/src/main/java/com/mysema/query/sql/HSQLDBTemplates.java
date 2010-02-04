/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.operation.Ops;

/**
 * HSQLDBTemplates is an SQL dialect for HSQLDB
 * 
 * @author tiwe
 * 
 */
public class HSQLDBTemplates extends SQLTemplates {
    {
        add(Ops.MathOps.ROUND, "round({0},0)");
        add(Ops.TRIM, "trim(both from {0})");
    }
}