package com.mysema.query.sql;

import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class SQLQueryMixin<T> extends QueryMixin<T> {
    
    public SQLQueryMixin() {}

    public SQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public SQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    public <P> T join(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T innerJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T rightJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T fullJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return getSelf();
    }

    
}
