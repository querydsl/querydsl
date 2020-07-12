package com.querydsl.r2dbc;

import io.r2dbc.spi.ConnectionMetadata;

public final class SQLTemplatesRegistryDump {

    private SQLTemplatesRegistryDump() {
    }

    public static void main(String[] args) {
        dump();
        Connections.initH2();
        dump();
        Connections.initMySQL();
        dump();
        Connections.initPostgreSQL();
        dump();
        Connections.initSQLServer();
        dump();
    }

    private static void dump() {
        ConnectionMetadata md = Connections.getConnection().getMetadata();
        System.out.println(md.getDatabaseProductName());
        System.out.println(md.getDatabaseVersion());
        System.out.println();
    }
}
