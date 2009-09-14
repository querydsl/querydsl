/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.serialization;

import com.mysema.query.jdoql.JDOQLSerializer;
import com.mysema.query.jdoql.JDOQLSubQuery;
import com.mysema.query.jdoql.JDOQLTemplates;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.SubQuery;

public abstract class AbstractTest {

    private static final JDOQLTemplates templates = new JDOQLTemplates();
    
    protected JDOQLSubQuery query(){
        return new JDOQLSubQuery();
    }
    
    protected String serialize(SubQuery expr) {
        Expr<?> source = expr.getMetadata().getJoins().get(0).getTarget();
        JDOQLSerializer serializer = new JDOQLSerializer(templates, source);
        serializer.serialize(expr.getMetadata(), false, false);
        String rv = serializer.toString().replace('\n', ' ');
//        System.out.println(rv);
        return rv;
    }

}
