/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


/**
 * Dialect provides different SQL dialects for querydsl-sql.
 * Querydsl SQL supports the following database systems :
 * <ul>
 *   <li>HSQLDB</li>
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
    public static SqlOps forHqlsdb(){
        return new SqlOps(){{                                  
            add(Ops.OpMath.ROUND, "round(%s,0)");
            add(Ops.TRIM, "trim(both from %s)");            
        }};        
    }
    
    // tested
    public static SqlOps forMySQL(){
        return new SqlOps();
    }
    
    public static SqlOps forOracle10(){
        return forOracle();
    }
    
    // tested
    public static SqlOps forOracle(){
        return new SqlOps(){{
            add(Ops.OpMath.CEIL, "ceil(%s)");  
            add(Ops.OpMath.RANDOM, "dbms_random.value");
            add(Ops.OpMath.LOG, "ln(%s)");  
            add(Ops.OpMath.LOG10, "log(10,%s)");
            
            add(Ops.CONCAT, "%s || %s");
            add(Ops.OpString.SPACE, "lpad('',%s,' ')");
                      
            add(Ops.OpDateTime.YEAR, "extract(year from %s)");
            add(Ops.OpDateTime.MONTH, "extract(month from %s)");
            add(Ops.OpDateTime.WEEK, "to_number(to_char(%s,'WW'))");
            add(Ops.OpDateTime.DAY, "extract(day from %s)");
            
            add(Ops.OpDateTime.HOUR, "to_number(to_char(%s,'HH24'))");
            add(Ops.OpDateTime.MINUTE, "to_number(to_char(%s,'MI'))");
            add(Ops.OpDateTime.SECOND, "to_number(to_char(%s,'SS'))");
            
            add(Ops.OpDateTime.DAY_OF_MONTH, "to_number(to_char(%s,'DD'))");
            add(Ops.OpDateTime.DAY_OF_WEEK, "to_number(to_char(%s,'D'))");
            add(Ops.OpDateTime.DAY_OF_YEAR, "to_number(to_char(%s,'DDD'))");
            
            limitAndOffsetSymbols(false);
            limitTemplate("rownum < %s");
            offsetTemplate("rownum > %s");
            limitOffsetTemplate("rownum between %1$s and %3$s");
        }};
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
