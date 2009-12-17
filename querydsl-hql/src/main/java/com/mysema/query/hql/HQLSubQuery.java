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
 * @author tiwe
 *
 */
public class HQLSubQuery extends QueryBaseWithDetach<HQLSubQuery>{
    
    private static final HQLTemplates templates = new HQLTemplates();

    public HQLSubQuery() {
        super(new DefaultQueryMetadata());
    }

    public HQLSubQuery from(PEntity<?>... o) {
        getMetadata().addFrom(o);
        return _this;
    }
    
    public String toString(){
        if (!getMetadata().getJoins().isEmpty()){
            HQLSerializer serializer = new HQLSerializer(templates);
            serializer.serialize(getMetadata(), false);
            return serializer.toString().trim();    
        }else{
            return super.toString();
        }        
    }
        
}
