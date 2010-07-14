package com.mysema.query.sql.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysema.query.QueryException;
import com.mysema.query.sql.SQLTemplates;

/**
 * @author tiwe
 *
 */
public class DropTableClause {

    private final Connection connection;
    
    private final String table;
    
    public DropTableClause(Connection conn, SQLTemplates templates, String table) {
        this.connection = conn;
        this.table = templates.quoteTableName(table);
    }
    
    public void execute(){
        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute("DROP TABLE " + table);
        } catch (SQLException e) {
            // do not throw
        }finally{
            if (stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new QueryException(e);
                }
            }            
        }  
    }
    
}
