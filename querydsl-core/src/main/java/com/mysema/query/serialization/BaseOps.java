/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.PathMetadata;

/**
 * HqlOps provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class BaseOps  {
    
    protected final Map<Op<?>,java.lang.String> patterns = new HashMap<Op<?>,java.lang.String>();
        
    public BaseOps(){       
        // boolean
        patterns.put(Ops.AND, "%s && %s");
        patterns.put(Ops.NOT, "!%s");
        patterns.put(Ops.OR, "%s || %s");
        patterns.put(Ops.XNOR, "!(%s ^ %s)");        
        patterns.put(Ops.XOR, "%s ^ %s");
        
        // comparison
        patterns.put(Ops.GOE, "%s >= %s");
        patterns.put(Ops.GT, "%s > %s");
        patterns.put(Ops.LOE, "%s <= %s");
        patterns.put(Ops.LT, "%s < %s");
           
        patterns.put(Ops.AFTER, "%s > %s");
        patterns.put(Ops.BEFORE, "%s < %s");
        
        // numeric
        patterns.put(Ops.ADD, "%s + %s");        
        patterns.put(Ops.DIV, "%s / %s");
        patterns.put(Ops.MOD, "%s % %s");
        patterns.put(Ops.MULT,"%s * %s");
        patterns.put(Ops.SUB, "%s - %s");
        
        // various
        patterns.put(Ops.EQ_PRIMITIVE, "%s == %s");
        patterns.put(Ops.EQ_OBJECT, "%s == %s");
        patterns.put(Ops.NE_PRIMITIVE, "%s != %s");        
        patterns.put(Ops.NE_OBJECT, "%s != %s");
        patterns.put(Ops.ISNULL, "%s == null");
        patterns.put(Ops.ISNOTNULL, "%s != null");
        
        // string
        patterns.put(Ops.CONCAT, "%s + %s");
           
        patterns.put(PathMetadata.PROPERTY,"%s.%s"); 
        patterns.put(PathMetadata.SIZE,"%s.size()");
        patterns.put(PathMetadata.VARIABLE,"%s"); 
        
    }

    public java.lang.String getPattern(Op<?> op){
        return patterns.get(op);
    }
    
}
