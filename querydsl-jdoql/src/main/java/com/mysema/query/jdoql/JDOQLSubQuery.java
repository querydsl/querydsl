/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;

/**
 * @author tiwe
 *
 */
public class JDOQLSubQuery extends QueryBaseWithDetach<JDOQLSubQuery>{

    public JDOQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public JDOQLSubQuery() {
        super(new DefaultQueryMetadata());
    }
    
    public JDOQLSubQuery from(PEntity<?>... o) {
        super.from(o);
        return _this;
    }

    public <P> JDOQLSubQuery from(PEntityCollection<P> target, PEntity<P> alias){
        super.from(OSimple.create(alias.getType(), Ops.ALIAS, target, alias));
        return _this;
    }
}
