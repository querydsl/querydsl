package com.mysema.query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Test;

public abstract class TypesBaseTest {
    
    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }
    
    @Test
    public void DumpTypes() throws SQLException {
        Connection conn = Connections.getConnection();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getUDTs(null, null, "%", null);
        try {
            while (rs.next()) {
                System.out.println(rs.getString(3) + " " + rs.getString(4));
            }
        } finally {
            rs.close();
        }
    }

}
