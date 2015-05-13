package com.querydsl.jpa;

import com.querydsl.core.Target;
import com.querydsl.sql.*;

/**
 * @author tiwe
 *
 */
public final class Mode {

    public static final ThreadLocal<String> mode = new ThreadLocal<String>();

    public static final ThreadLocal<Target> target = new ThreadLocal<Target>();

    public static SQLTemplates getSQLTemplates() {
        switch (target.get()) {
        case CUBRID: return new CUBRIDTemplates();
        case DERBY:  return new DerbyTemplates();
        case H2:     return new H2Templates();
        case HSQLDB: return new HSQLDBTemplates();
        case SQLSERVER: return new SQLServerTemplates();
        case MYSQL:  return new MySQLTemplates();
        case ORACLE: return new OracleTemplates();
        case POSTGRESQL: return new PostgreSQLTemplates();
        case SQLITE: return new SQLiteTemplates();
        case TERADATA: return new TeradataTemplates();
        }
        throw new IllegalStateException("Unknown mode " + mode);
    }

    private Mode() { }

}
