package com.querydsl.sql.spatial.suites;

import java.sql.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.H2;
import com.querydsl.sql.Connections;

@Category(H2.class)
public class SpatialTest {

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Connections.initH2();
//      Connections.initMySQL();
//      Connections.initPostgreSQL();
//      Connections.initTeradata();
    }

    @After
    public void tearDown() throws SQLException {
        Connections.close();
    }

    @Test
    public void test() throws SQLException {
        Statement stmt = Connections.getStatement();
        try (ResultSet rs = stmt.executeQuery("select \"GEOMETRY\" from \"SHAPES\"")) {
            while (rs.next()) {
                System.err.println(rs.getObject(1).getClass().getName());
                System.err.println(rs.getString(1));
//                Clob clob = rs.getClob(1);
//                System.err.println(clob.getSubString(1, (int) clob.length()));
            }
        }
    }

    @Test
    public void metadata() throws SQLException {
        Connection conn = Connections.getConnection();
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getColumns(null, null, "SHAPES", "GEOMETRY")) {
            rs.next();
            int type = rs.getInt("DATA_TYPE");
            String typeName = rs.getString("TYPE_NAME");
            System.err.println(type + " " + typeName);
        }
    }

}
