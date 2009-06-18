package com.mysema.query.jdoql.serialization;

import com.mysema.query.jdoql.JDOQLPatterns;
import com.mysema.query.jdoql.JDOQLQuery;
import com.mysema.query.jdoql.JDOQLQueryImpl;
import com.mysema.query.jdoql.JDOQLSerializer;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.SubQuery;

public abstract class AbstractTest {
    
    protected JDOQLQuery query(){
        // creates detached query
        return new JDOQLQueryImpl(null);
    }

    protected String serialize(SubQuery expr) {
        Expr<?> source = expr.getMetadata().getJoins().get(0).getTarget();
        JDOQLSerializer serializer = new JDOQLSerializer(JDOQLPatterns.DEFAULT, source);
        serializer.serialize(expr.getMetadata(), false, false);
        String rv = serializer.toString().replace('\n', ' ');
//        System.out.println(rv);
        return rv;
    }

}
