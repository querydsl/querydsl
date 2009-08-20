/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.sql.oracle.OracleTemplates;
import com.mysema.query.types.operation.Ops;

/**
 * SQLTemplatesFactory provides different SQL dialects for querydsl-sql. Querydsl SQL
 * supports the following database systems :
 * <ul>
 * <li>HSQLDB</li>
 * <li>Derby</li>
 * <li>MySQL</li>
 * <li>Oracle</li>
 * <li>PostgreSQL</li>
 * <li>SQL Server</li>
 * </ul>
 * 
 * @author tiwe
 * @version $Id$
 */
public final class SQLTemplatesFactory {

    private SQLTemplatesFactory(){}
    
    private static final SQLTemplates defaultTemplates = new SQLTemplates();
    
    private static final SQLTemplates derbyTemplates = new SQLTemplates() {
        {
            add(Ops.CONCAT, "{0} || {1}");
            add(Ops.MathOps.ROUND, "floor({0})");
            add(Ops.SUBSTR_1ARG, "substr({0},{1}+1)");
            add(Ops.SUBSTR_2ARGS, "substr({0},{1}+1,{2}+1)");

            add(Ops.STARTS_WITH, "{0} like ({1} || '%')");
            add(Ops.ENDS_WITH, "{0} like ('%' || {1})");
            add(Ops.STARTS_WITH_IC, "lower({0}) like (lower({1}) || '%')");
            add(Ops.ENDS_WITH_IC, "lower({0}) like ('%' || lower({1}))");

            add(Ops.DateTimeOps.YEAR, "year({0})");
            add(Ops.DateTimeOps.MONTH, "month({0})");

            add(Ops.DateTimeOps.HOUR, "hour({0})");
            add(Ops.DateTimeOps.MINUTE, "minute({0})");
            add(Ops.DateTimeOps.SECOND, "second({0})");
        }
    };
    
    private static final SQLTemplates hsqldbTemplates = new SQLTemplates() {
        {
            add(Ops.MathOps.ROUND, "round({0},0)");
            add(Ops.TRIM, "trim(both from {0})");
        }
    };
    
    private static final SQLTemplates mysqlTemplates = new SQLTemplates() {
        {
            addClass2TypeMappings("signed", Byte.class, Integer.class,
                    Long.class, Short.class, BigInteger.class);
            addClass2TypeMappings("decimal", Double.class, Float.class,
                    BigDecimal.class);
            addClass2TypeMappings("char(256)", String.class);
        }
    };

    private static final SQLTemplates oracleTemplates = new OracleTemplates();

    public static SQLTemplates forDerby() {
        return derbyTemplates;
    }

    public static SQLTemplates forHSQLDB() {
        return hsqldbTemplates;
    }

    public static SQLTemplates forMySQL() {
        return mysqlTemplates;
    }

    public static SQLTemplates forOracle() {
        return oracleTemplates;
    }

    public static SQLTemplates forOracle10() {
        return oracleTemplates;
    }
    
    // TODO : to be tested
    public static SQLTemplates forPostgreSQL() {
        return defaultTemplates;
    }
    
    // TODO : to be tested
    public static SQLTemplates forSQLServer() {
        return defaultTemplates;
    }

}
