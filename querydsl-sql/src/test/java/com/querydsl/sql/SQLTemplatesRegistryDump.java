package com.querydsl.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class SQLTemplatesRegistryDump {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connections.initCubrid();
        dump();
        Connections.initDerby();
        dump();
        Connections.initFirebird();
        dump();
        Connections.initH2();
        dump();
        Connections.initHSQL();
        dump();
        Connections.initMySQL();
        dump();
        Connections.initOracle();
        dump();
        Connections.initPostgres();
        dump();
        Connections.initSQLite();
        dump();
        Connections.initSQLServer();
        dump();
        /*Connections.initTeradata();
        dump();*/
    }

    private static void dump() throws SQLException {
        DatabaseMetaData md = Connections.getConnection().getMetaData();
        System.out.println(md.getDatabaseProductName());
        System.out.println(md.getDatabaseMajorVersion());
        System.out.println(md.getDatabaseMinorVersion());
        System.out.println();
        Connections.close();
    }
}
