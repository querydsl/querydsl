package com.mysema.query.sql.mysql;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
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
 * MySQL specific implementation of SQLQueryFactory
 *
 * @author tiwe
 *
 */
public class MySQLQueryFactory implements SQLQueryFactory<MySQLQuery, SQLSubQuery, SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause>{

    private final SQLQueryFactoryImpl queryFactory;

    public MySQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        queryFactory = new SQLQueryFactoryImpl(configuration, connection);
    }

    public MySQLQueryFactory(Provider<Connection> connection) {
        this(new Configuration(new MySQLTemplates()), connection);
    }

    public MySQLQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        this(new Configuration(templates), connection);
    }

    public SQLDeleteClause delete(RelationalPath<?> path) {
        return queryFactory.delete(path);
    }

    public MySQLQuery from(Expression<?> from) {
        return query().from(from);
    }

    public SQLInsertClause insert(RelationalPath<?> path) {
        return queryFactory.insert(path);
    }

    public SQLInsertClause insertIgnore(RelationalPath<?> entity) {
        SQLInsertClause insert = insert(entity);
        insert.addFlag(Position.START_OVERRIDE, "insert ignore into ");
        return insert;
    }

    public SQLMergeClause merge(RelationalPath<?> path) {
        return queryFactory.merge(path);
    }

    public MySQLQuery query() {
        return new MySQLQuery(queryFactory.getConnection(), queryFactory.getConfiguration());
    }

    public SQLInsertClause replace(RelationalPath<?> entity) {
        SQLInsertClause insert = insert(entity);
        insert.addFlag(Position.START_OVERRIDE, "replace into ");
        return insert;
    }

    public SQLSubQuery subQuery() {
        return queryFactory.subQuery();
    }

    public SQLSubQuery subQuery(Expression<?> from){
        return subQuery().from(from);
    }

    public SQLUpdateClause update(RelationalPath<?> path) {
        return queryFactory.update(path);
    }


}
