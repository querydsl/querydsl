package com.mysema.query.jpa;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.Configuration;

public class SQLSubQuery extends AbstractSQLSubQuery<SQLSubQuery> {

    public SQLSubQuery() {
        super();
    }
    
    public SQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

    public SQLSubQuery(Configuration configuration, QueryMetadata metadata) {
        super(metadata);
    }

    @Override
    public SQLSubQuery clone() {
        SQLSubQuery subQuery = new SQLSubQuery(configuration, getMetadata());
        return subQuery;
    }

}