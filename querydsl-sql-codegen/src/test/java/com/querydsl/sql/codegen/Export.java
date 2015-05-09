package com.querydsl.sql.codegen;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public final class Export {

    private Export() {}

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/querydsl";
        Connection conn = DriverManager.getConnection(url, "querydsl", "querydsl");

        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix("S");
        exporter.setPackageName("com.querydsl.jpa.domain.sql");
        exporter.setTargetFolder(new File("../querydsl-jpa/src/test/java"));
//        exporter.setLowerCase(true);
        exporter.export(conn.getMetaData());

        conn.close();
    }

}
