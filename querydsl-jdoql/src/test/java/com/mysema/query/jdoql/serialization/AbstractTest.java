/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.serialization;

import com.mysema.query.jdoql.JDOQLSerializer;
import com.mysema.query.jdoql.JDOQLSubQuery;
import com.mysema.query.jdoql.JDOQLTemplates;
import com.mysema.query.types.Expression;
import com.mysema.query.types.SubQueryExpression;

public abstract class AbstractTest {

    protected JDOQLSubQuery query(){
        return new JDOQLSubQuery();
    }

    protected String serialize(SubQueryExpression expr) {
        Expression<?> source = expr.getMetadata().getJoins().get(0).getTarget();
        JDOQLSerializer serializer = new JDOQLSerializer(JDOQLTemplates.DEFAULT, source);
        serializer.serialize(expr.getMetadata(), false, false);
        String rv = serializer.toString().replace('\n', ' ');
//        System.out.println(rv);
        return rv;
    }

}
