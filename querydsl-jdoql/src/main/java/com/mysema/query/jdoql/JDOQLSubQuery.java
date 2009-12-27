/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 */
public class JDOQLSubQuery extends QueryBaseWithDetach<JDOQLSubQuery>{
    

    public JDOQLSubQuery() {
        this(new DefaultQueryMetadata());
    }
    
    public JDOQLSubQuery(QueryMetadata metadata) {
        super(new JDOQLQueryMixin<JDOQLSubQuery>(metadata));
        this.queryMixin.setSelf(this);
    }
    
    public JDOQLSubQuery from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public <P> JDOQLSubQuery from(Path<? extends Collection<P>> target, PEntity<P> alias){
        queryMixin.getMetadata().addFrom(OSimple.create(alias.getType(), Ops.ALIAS, target.asExpr(), alias));
        return this;
    }
    
    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(JDOQLTemplates.DEFAULT, source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        } 
    }
}
