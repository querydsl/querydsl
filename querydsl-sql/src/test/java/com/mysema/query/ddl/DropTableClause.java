/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysema.query.QueryException;
import com.mysema.query.sql.Configuration;

/**
 * DropTableClause defines a DROP TABLE clause
 * 
 * @author tiwe
 *
 */
public class DropTableClause {

    private final Connection connection;
    
    private final String table;
    
    public DropTableClause(Connection conn, Configuration c, String table) {
        this.connection = conn;
        this.table = c.getTemplates().quoteIdentifier(table);
    }
    
    @SuppressWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    public void execute() {
        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute("DROP TABLE " + table);
        } catch (SQLException e) {
            // do not rethrow
        }finally{
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new QueryException(e);
                }
            }            
        }  
    }
    
}
    