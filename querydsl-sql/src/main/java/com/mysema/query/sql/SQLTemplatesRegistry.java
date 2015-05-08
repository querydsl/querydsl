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
package com.mysema.query.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 */
public class SQLTemplatesRegistry {

    private final SQLTemplates generic = SQLTemplates.DEFAULT;

    private final SQLTemplates cubrid = new CUBRIDTemplates();
    private final SQLTemplates derby = new DerbyTemplates();
    private final SQLTemplates firebird = new FirebirdTemplates();
    private final SQLTemplates h2 = new H2Templates();
    private final SQLTemplates hsqldb = new HSQLDBTemplates();
    private final SQLTemplates mysql = new MySQLTemplates();
    private final SQLTemplates oracle = new OracleTemplates();
    private final SQLTemplates postgres = new PostgresTemplates();
    private final SQLTemplates sqlite = new SQLiteTemplates();
    private final SQLTemplates teradata = new TeradataTemplates();

    private final SQLTemplates sqlserver = new SQLServerTemplates();
    private final SQLTemplates sqlserver2005 = new SQLServer2005Templates();
    private final SQLTemplates sqlserver2008 = new SQLServer2008Templates();
    private final SQLTemplates sqlserver2012 = new SQLServer2012Templates();

    public SQLTemplates getTemplates(DatabaseMetaData md) throws SQLException {
        String name = md.getDatabaseProductName().toLowerCase();
        if (name.equals("cubrid")) return cubrid;
        if (name.equals("apache derby")) return derby;
        if (name.startsWith("firebird")) return firebird;
        if (name.equals("h2")) return h2;
        if (name.equals("hsql")) return hsqldb;
        if (name.equals("mysql")) return mysql;
        if (name.equals("oracle")) return oracle;
        if (name.equals("postgresql")) return postgres;
        if (name.equals("sqlite")) return sqlite;
        if (name.startsWith("teradata")) return teradata;

        // sqlserver
        if (name.equals("microsft sql server")) {
            switch (md.getDatabaseMajorVersion()) {
                case 12:
                case 11: return sqlserver2012;
                case 10: return sqlserver2008;
                case 9:  return sqlserver2005;
                default: return sqlserver;
            }
        }

        return generic;
    }

}
