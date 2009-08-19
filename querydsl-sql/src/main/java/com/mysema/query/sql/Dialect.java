/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.sql.oracle.OracleDialect;
import com.mysema.query.types.operation.Ops;

/**
 * Dialect provides different SQL dialects for querydsl-sql. Querydsl SQL
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
public class Dialect {

    // tested
    public static SQLTemplates forHSQLDB() {
        return new SQLTemplates() {
            {
                add(Ops.MathOps.ROUND, "round({0},0)");
                add(Ops.TRIM, "trim(both from {0})");
            }
        };
    }

    // tested
    public static SQLTemplates forDerby() {
        return new SQLTemplates() {
            {
                add(Ops.CONCAT, "{0} || {1}");
                add(Ops.MathOps.ROUND, "floor({0})");
                add(Ops.SUBSTR1ARG, "substr({0},{1}+1)");
                add(Ops.SUBSTR2ARGS, "substr({0},{1}+1,{2}+1)");

                add(Ops.STARTSWITH, "{0} like ({1} || '%')");
                add(Ops.ENDSWITH, "{0} like ('%' || {1})");
                add(Ops.STARTSWITH_IC, "lower({0}) like (lower({1}) || '%')");
                add(Ops.ENDSWITH_IC, "lower({0}) like ('%' || lower({1}))");

                add(Ops.DateTimeOps.YEAR, "year({0})");
                add(Ops.DateTimeOps.MONTH, "month({0})");

                add(Ops.DateTimeOps.HOUR, "hour({0})");
                add(Ops.DateTimeOps.MINUTE, "minute({0})");
                add(Ops.DateTimeOps.SECOND, "second({0})");
            }
        };
    }

    // tested
    public static SQLTemplates forMySQL() {
        return new SQLTemplates() {
            {
                addClass2TypeMappings("signed", Byte.class, Integer.class,
                        Long.class, Short.class, BigInteger.class);
                addClass2TypeMappings("decimal", Double.class, Float.class,
                        BigDecimal.class);
                addClass2TypeMappings("char(256)", String.class);
            }
        };
    }

    public static SQLTemplates forOracle10() {
        return forOracle();
    }

    // tested
    public static SQLTemplates forOracle() {
        return new OracleDialect();
    }

    // TODO : to be tested
    public static SQLTemplates forPostgreSQL() {
        return new SQLTemplates();
    }

    // TODO : to be tested
    public static SQLTemplates forSQLServer() {
        return new SQLTemplates();
    }

}
