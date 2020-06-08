package com.querydsl.r2dbc;

import io.r2dbc.spi.Connection;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public class MetadataTest {

//    H2
//    MySQL
//    Oracle
//    PostgreSQL

    @Test
    public void test() {
        Connections.initH2();
        printMetadata();
        Connections.initMySQL();
        printMetadata();
        Connections.initPostgreSQL();
        printMetadata();
        Connections.initSQLServer();
        printMetadata();
    }

    private void printMetadata() {
        Connection conn = Connections.getConnection();
        System.out.println(conn.getMetadata().getDatabaseProductName());
    }

}
