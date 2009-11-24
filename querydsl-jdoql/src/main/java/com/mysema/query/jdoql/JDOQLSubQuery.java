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
    
    private static final JDOQLTemplates templates = new JDOQLTemplates();

    public JDOQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public JDOQLSubQuery() {
        super(new DefaultQueryMetadata());
    }
    
    public JDOQLSubQuery from(PEntity<?>... o) {
        getMetadata().addFrom(o);
        return _this;
    }

    public <P> JDOQLSubQuery from(Path<? extends Collection<P>> target, PEntity<P> alias){
        getMetadata().addFrom(OSimple.create(alias.getType(), Ops.ALIAS, target.asExpr(), alias));
        return _this;
    }
    
    @Override
    public String toString(){
        if (!getMetadata().getJoins().isEmpty()){
            Expr<?> source = this.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(templates, source);
            serializer.serialize(getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        } 
    }
}
