/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.path.PEntity;

/**
 * HQLSubQuery is a subquery builder class for HQL/JPAQL
 * 
 * @author tiwe
 *
 */
public class HQLSubQuery extends QueryBaseWithDetach<HQLSubQuery>{

    public HQLSubQuery() {
        super(new DefaultQueryMetadata());
    }

    public HQLSubQuery from(PEntity<?>... o) {
        getMetadata().addFrom(o);
        return _this;
    }
    
    public String toString(){
        if (!getMetadata().getJoins().isEmpty()){
            HQLSerializer serializer = new HQLSerializer(HQLTemplates.DEFAULT);
            serializer.serialize(getMetadata(), false, null);
            return serializer.toString().trim();    
        }else{
            return super.toString();
        }        
    }
        
}
