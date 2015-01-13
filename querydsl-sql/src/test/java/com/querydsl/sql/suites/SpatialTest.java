package com.querydsl.sql.suites;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.Connections;

public class SpatialTest {

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Connections.initH2();
//      Connections.initMySQL();
//      Connections.initPostgres();
//      Connections.initTeradata();
    }

    @After
    public void tearDown() throws SQLException {
        Connections.close();
    }

    @Test
    public void test() throws SQLException {
        Statement stmt = Connections.getStatement();
        ResultSet rs = stmt.executeQuery("select \"GEOMETRY\" from \"SHAPES\"");
        try {
            while (rs.next()) {
                System.err.println(rs.getObject(1).getClass().getName());
                System.err.println(rs.getString(1));
//                Clob clob = rs.getClob(1);
//                System.err.println(clob.getSubString(1, (int) clob.length()));
            }
        } finally {
            rs.close();
        }
    }

    @Test
    public void Metadata() throws SQLException {
        Connection conn = Connections.getConnection();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getColumns(null, null, "SHAPES", "GEOMETRY");
        try {
            rs.next();
            int type = rs.getInt("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            System.err.println(type + " " + typeName);
        } finally {
            rs.close();
        }
    }

}
