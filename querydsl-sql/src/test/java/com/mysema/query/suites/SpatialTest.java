package com.mysema.query.suites;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.Connections;

public class SpatialTest {

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        //Connections.initTeradata();
        Connections.initPostgres();
//        Connection conn = Connections.getConnection();
//        ((org.postgresql.Connection)conn).addDataType("geometry","org.postgis.PGgeometry");
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

}
