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
 * Dialect provides different SQL dialects for querydsl-sql.
 * Querydsl SQL supports the following database systems :
 * <ul>
 *   <li>HSQLDB</li>
 *   <li>Derby</li>
 *   <li>MySQL</li>
 *   <li>Oracle</li>
 *   <li>PostgreSQL</li>
 *   <li>SQL Server</li>
 * </ul>
 *
 * @author tiwe
 * @version $Id$
 */
public class Dialect {
    
    // tested
    public static SQLOps forHSQLDB(){
        return new SQLOps(){{                                  
            add(Ops.OpMath.ROUND, "round(%s,0)");
            add(Ops.TRIM, "trim(both from %s)");
        }};        
    }
    
    // tested
    public static SQLOps forDerby(){
    	return new SQLOps(){{
    		add(Ops.CONCAT, "%s || %s");
    		add(Ops.OpMath.ROUND, "floor(%s)");
    		add(Ops.SUBSTR1ARG,   "substr(%s,%s+1)");
    		add(Ops.SUBSTR2ARGS,  "substr(%s,%s+1,%s+1)");
    		
            add(Ops.STARTSWITH, "%s like (%s || '%%')");
            add(Ops.ENDSWITH, "%s like ('%%' || %s)");
            add(Ops.STARTSWITH_IC, "lower(%s) like (lower(%s) || '%%')");
            add(Ops.ENDSWITH_IC, "lower(%s) like ('%%' || lower(%s))");
            
            add(Ops.OpDateTime.YEAR, "year(%s)");
            add(Ops.OpDateTime.MONTH, "month(%s)");
            
            add(Ops.OpDateTime.HOUR, "hour(%s)");
            add(Ops.OpDateTime.MINUTE, "minute(%s)");
            add(Ops.OpDateTime.SECOND, "second(%s)");
    	}};
    }
    
    // tested
    public static SQLOps forMySQL(){
        return new SQLOps(){{
            addClass2TypeMappings("signed", Byte.class, Integer.class, Long.class,
                    Short.class, BigInteger.class);
            addClass2TypeMappings("decimal", Double.class, Float.class, BigDecimal.class);
            addClass2TypeMappings("char(256)", String.class);
        }};
    }
    
    public static SQLOps forOracle10(){
        return forOracle();
    }
    
    // tested
    public static SQLOps forOracle(){
        return new OracleDialect();
    }
    
    // TODO : to be tested
    public static SQLOps forPostgreSQL(){
        return new SQLOps();
    }

    // TODO : to be tested
    public static SQLOps forSQLServer(){
        return new SQLOps();
    }
    
           
}
