/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.math.BigDecimal;
import java.math.BigInteger;


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
    public static SqlOps forHSQLDB(){
        return new SqlOps(){{                                  
            add(Ops.OpMath.ROUND, "round(%s,0)");
            add(Ops.TRIM, "trim(both from %s)");
        }};        
    }
    
    // tested
    public static SqlOps forDerby(){
    	return new SqlOps(){{
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
    public static SqlOps forMySQL(){
        return new SqlOps(){{
            addClass2TypeMappings("signed", Byte.class, Integer.class, Long.class,
                    Short.class, BigInteger.class);
            addClass2TypeMappings("decimal", Double.class, Float.class, BigDecimal.class);
            addClass2TypeMappings("char(256)", String.class);
        }};
    }
    
    public static SqlOps forOracle10(){
        return forOracle();
    }
    
    // tested
    public static SqlOps forOracle(){
        return new OracleDialect();
    }
    
    // TODO : to be tested
    public static SqlOps forPostgreSQL(){
        return new SqlOps();
    }

    // TODO : to be tested
    public static SqlOps forSQLServer(){
        return new SqlOps();
    }
    
           
}
