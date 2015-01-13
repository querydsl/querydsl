package com.querydsl.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class OracleTimeTest {

    @After
    public void tearDown() throws SQLException {
        Connections.close();
    }

    @Test
    public void test() throws ClassNotFoundException, SQLException {
        Connections.initOracle();
        Connection conn = Connections.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select timefield from employee");
        rs.next();
        Object o = rs.getObject(1);
        System.err.println(o.getClass().getName() + ": " + o);
        o = rs.getTimestamp(1);
        System.err.println(o.getClass().getName() + ": " + o);
        o = rs.getTime(1);
        System.err.println(o.getClass().getName() + ": " + o);
        rs.close();
        stmt.close();
    }

}
