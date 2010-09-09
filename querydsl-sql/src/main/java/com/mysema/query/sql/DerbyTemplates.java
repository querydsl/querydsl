/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Ops;

/**
 * DerbyTemplates is an SQL dialect for Derby
 *
 * @author tiwe
 *
 */
public class DerbyTemplates extends SQLTemplates {

    private String limitOffsetTemplate = "\noffset {1s} rows fetch next {0s} rows only";

    private String limitTemplate = "\nfetch first {0s} rows only";

    private String offsetTemplate = "\noffset {0s} rows";

    public DerbyTemplates(){
        this(false);
    }

    public DerbyTemplates(boolean quote){
        super("\"", quote);
        addClass2TypeMappings("smallint", Byte.class);
        setAutoIncrement(" generated always as identity");
        
        add(Ops.CONCAT, "varchar({0} || {1})");
        add(Ops.MathOps.ROUND, "floor({0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "day({0})");

        // case for eq
        add(Ops.CASE_EQ, "case {1} end");
        add(Ops.CASE_EQ_WHEN,  "when {0} = {1} then {2} {3}");
        add(Ops.CASE_EQ_ELSE,  "else {0}");

    }

    protected void serializeModifiers(QueryMetadata metadata, SerializationContext context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() == null){
            context.handle(offsetTemplate, mod.getOffset());
        }else if (mod.getOffset() == null){
            context.handle(limitTemplate, mod.getLimit());
        }else{
            context.handle(limitOffsetTemplate, mod.getLimit(), mod.getOffset());
        }
    }

}
