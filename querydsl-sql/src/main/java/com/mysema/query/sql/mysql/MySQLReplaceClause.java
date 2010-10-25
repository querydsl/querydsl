package com.mysema.query.sql.mysql;

import java.sql.Connection;

import com.mysema.query.QueryFlag.Position;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * @author tiwe
 *
 */
public class MySQLReplaceClause extends SQLInsertClause{
    
    public MySQLReplaceClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        super(connection, templates, entity);
        addFlag(Position.START_OVERRIDE, "replace into ");
    }
    
    public MySQLReplaceClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(connection, configuration, entity);
        addFlag(Position.START_OVERRIDE, "replace into ");
    }

}
