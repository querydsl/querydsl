package com.mysema.query.sql.dml;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AbstractDMLClause {
    
    protected void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }        
    }

}
