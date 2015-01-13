package com.querydsl.jpa;

import com.querydsl.core.Target;
import com.querydsl.sql.CUBRIDTemplates;
import com.querydsl.sql.DerbyTemplates;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.PostgresTemplates;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLiteTemplates;
import com.querydsl.sql.TeradataTemplates;

/**
 * @author tiwe
 *
 */
public final class Mode {

    public static final ThreadLocal<String> mode = new ThreadLocal<String>();

    public static final ThreadLocal<Target> target = new ThreadLocal<Target>();

    public static SQLTemplates getSQLTemplates() {
        switch (target.get()) {
        case CUBRID:return new CUBRIDTemplates();
        case DERBY: return new DerbyTemplates();
        case H2:    return new H2Templates();
        case HSQLDB:return new HSQLDBTemplates();
        case SQLSERVER: return new SQLServerTemplates();
        case MYSQL: return new MySQLTemplates();
        case ORACLE:return new OracleTemplates();
        case POSTGRES: return new PostgresTemplates();
        case SQLITE:return new SQLiteTemplates();
        case TERADATA: return new TeradataTemplates();
        }
        throw new IllegalStateException("Unknown mode " + mode);
    }

    private Mode() {}

}
