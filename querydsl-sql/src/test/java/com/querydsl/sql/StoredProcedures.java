/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

import java.sql.*;

public final class StoredProcedures {

    private StoredProcedures() { }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/procedure_test;create=true";

        try (Connection connection = DriverManager.getConnection(url, "", "")) {
            DatabaseMetaData md = connection.getMetaData();
            try (ResultSet procedures = md.getProcedures(null, null, null)) {
                while (procedures.next()) {
                    String cat = procedures.getString(1);
                    String schema = procedures.getString(2);
                    String name = procedures.getString(3);
                    String remarks = procedures.getString(7);
                    String type = procedures.getString(8);
                    String specificName = procedures.getString(9);
                    System.out.println(name + "\n" + remarks + "\n" + type + "\n" + specificName);

                    try (ResultSet procedureColumns = md.getProcedureColumns(cat, schema, name, null)) {
                        while (procedureColumns.next()) {
                            String columnName = procedureColumns.getString(4);
                            int columnType = procedureColumns.getInt(5);
                            int dataType = procedureColumns.getInt(6);
                            String typeName = procedureColumns.getString(7);
                            short nullable = procedureColumns.getShort(12);
                            System.out.println(" " + columnName + " " + columnType + " " + dataType + " " + typeName + " " + nullable);
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

}
