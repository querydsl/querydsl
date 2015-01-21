package com.querydsl.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MetadataTest {
    
//    CUBRID
//    Apache Derby
//    H2
//    HSQL Database Engine
//    MySQL
//    Oracle
//    PostgreSQL
//    SQLite
    
    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Connections.initCubrid();
        printMetadata();
        Connections.initDerby();
        printMetadata();
        Connections.initH2();
        printMetadata();
        Connections.initHSQL();
        printMetadata();
        Connections.initMySQL();
        printMetadata();
        Connections.initOracle();
        printMetadata();
        Connections.initPostgres();
        printMetadata();
        Connections.initSQLite();
        printMetadata();
//        Connections.initSQLServer()
//        printMetadata();
    }

    private void printMetadata() throws SQLException {
        Connection conn = Connections.getConnection();
        System.out.println(conn.getMetaData().getDatabaseProductName());
        Connections.close();
    }

}
