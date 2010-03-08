package com.mysema.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author tiwe
 *
 */
public final class Connections {
    
    private Connections(){}
    
    public static Connection getDerby() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/demoDB;create=true";
        return DriverManager.getConnection(url, "", "");
    }
    
    public static Connection getHSQL() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:target/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

    public static Connection getMySQL() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/querydsl";
        return DriverManager.getConnection(url, "root", "");
    }
}
