/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.types.operation.Ops;

/**
 * @author tiwe
 *
 */
public class DerbyTemplates extends SQLTemplates {
    {
        addClass2TypeMappings("smallint", Byte.class);
        
        add(Ops.CONCAT, "varchar({0} || {1})");
        add(Ops.MathOps.ROUND, "floor({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");
    }
}