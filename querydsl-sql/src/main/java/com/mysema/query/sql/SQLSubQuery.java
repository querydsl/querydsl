package com.mysema.query.sql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;

/**
 * @author tiwe
 *
 */
public class SQLSubQuery extends QueryBaseWithDetach<SQLSubQuery>{

    public SQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public SQLSubQuery() {
        super(new DefaultQueryMetadata());
    }
    
//    SQLQuery from(PEntity<?>... o);
//
//    SQLQuery fullJoin(Expr<?> o);
//
//    SQLQuery innerJoin(Expr<?> o);
//
//    SQLQuery join(Expr<?> o);
//
//    SQLQuery leftJoin(Expr<?> o);
//    
//    SQLQuery on(EBoolean condition);

}
