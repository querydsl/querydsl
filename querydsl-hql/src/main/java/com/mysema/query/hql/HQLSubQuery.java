package com.mysema.query.hql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
public class HQLSubQuery extends QueryBaseWithDetach<HQLSubQuery>{

    public HQLSubQuery() {
        super(new DefaultQueryMetadata());
    }

    public HQLSubQuery from(PEntity<?>... o) {
        super.from(o);
        return _this;
    }
        
}
