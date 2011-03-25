package com.mysema.query.sql.oracle;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.sql.SQLQueryFactoryImpl;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

/**
 * Oracle specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class OracleQueryFactory implements SQLQueryFactory<OracleQuery, SQLSubQuery, SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause>{

    private final SQLQueryFactoryImpl queryFactory;
    
    public OracleQueryFactory(Configuration configuration, Provider<Connection> connection) {
        queryFactory = new SQLQueryFactoryImpl(configuration, connection);
    }

    public OracleQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new OracleTemplates()), connection);
    }

    public OracleQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public SQLDeleteClause delete(RelationalPath<?> path) {
        return queryFactory.delete(path);
    }

    public OracleQuery from(Expression<?> from) {
        return query().from(from);
    }

    public SQLInsertClause insert(RelationalPath<?> path) {
        return queryFactory.insert(path);
    }


    public SQLMergeClause merge(RelationalPath<?> path) {
        return queryFactory.merge(path);
    }

    public OracleQuery query() {
        return new OracleQuery(queryFactory.getConnection(), queryFactory.getConfiguration());
    }

    public SQLSubQuery subQuery() {
        return queryFactory.subQuery();
    }

    public SQLUpdateClause update(RelationalPath<?> path) {
        return queryFactory.update(path);
    }


}
