package com.mysema.query.jpa;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.DetachableSQLQuery;
import com.mysema.query.sql.SQLSerializer;

public abstract class AbstractSQLSubQuery<Q extends AbstractSQLSubQuery<Q>> extends DetachableSQLQuery<Q> {
    
    public AbstractSQLSubQuery() {
        super();
    }

    public AbstractSQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }
    
    public AbstractSQLSubQuery(Configuration configuration, QueryMetadata metadata) {
        super(configuration, metadata);
    }

    @Override
    protected SQLSerializer createSerializer() {
        return new NativeSQLSerializer(configuration, true);
    }

}
