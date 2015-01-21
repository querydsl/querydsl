package com.querydsl.sql;

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
