package com.mysema.query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoredProcedures {
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/procedure_test;create=true";
        Connection connection = DriverManager.getConnection(url, "", "");
        
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet procedures = md.getProcedures(null, null, null);
            try {
                while (procedures.next()) {
                    String cat = procedures.getString(1);
                    String schema = procedures.getString(2);
                    String name = procedures.getString(3);
                    String remarks = procedures.getString(7);
                    String type = procedures.getString(8);
                    String specificName = procedures.getString(9);
                    System.out.println(name + "\n" + remarks + "\n" + type + "\n" + specificName);
                    
                    ResultSet procedureColumns = md.getProcedureColumns(cat, schema, name, null);
                    try {
                        while (procedureColumns.next()) {
                            String columnName = procedureColumns.getString(4);
                            int columnType = procedureColumns.getInt(5);
                            int dataType = procedureColumns.getInt(6);
                            String typeName = procedureColumns.getString(7);
                            short nullable = procedureColumns.getShort(12);
                            System.out.println(" " + columnName + " " + columnType + " " + dataType + " " + typeName + " " + nullable);
                        }
                        System.out.println();
                    } finally {
                        procedureColumns.close();
                    }
                }
            } finally {
                procedures.close();
            }
        } finally {
            connection.close();
        }
    }

}
