package com.mysema.query.sql.mysql;

import java.sql.Connection;

import javax.inject.Provider;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQueryFactory;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * MySQL specific subclass of SQLQueryFactory with additional insert ignore and replace into support
 *
 * @author tiwe
 *
 */
public class MySQLQueryFactory extends SQLQueryFactory{

    public MySQLQueryFactory(Provider<Connection> connection) {
        super(new MySQLTemplates(), connection);
    }

    public MySQLQueryFactory(SQLTemplates templates, Provider<Connection> connection) {
        super(templates, connection);
    }

    public MySQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        super(configuration, connection);
    }

    public SQLInsertClause insertIgnore(RelationalPath<?> entity) {
        SQLInsertClause insert = insert(entity);
        insert.addFlag(Position.START_OVERRIDE, "insert ignore into ");
        return insert;
    }

    public SQLInsertClause replace(RelationalPath<?> entity) {
        SQLInsertClause insert = insert(entity);
        insert.addFlag(Position.START_OVERRIDE, "replace into ");
        return insert;
    }


}
