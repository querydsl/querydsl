/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


/**
 * Dialects provides different SQL dialects for querydsl-sql
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
    
    // tested
    public static SqlOps forOracle(){
        return new SqlOps(){{
            add(Ops.OpMath.CEIL, "ceil(%s)");  
            add(Ops.OpMath.RANDOM, "dbms_random.value");
            add(Ops.OpMath.LOG, "ln(%s)");  
            add(Ops.OpMath.LOG10, "log(10,%s)");
            add(Ops.SUBSTR1ARG, "substr(%s,%s)");
            add(Ops.SUBSTR2ARGS, "substr(%s,%s,%s)");
            add(Ops.CONCAT, "%s || %s");
            add(Ops.OpString.SPACE, "lpad('',%s,' ')");
            add(Ops.OpDateTime.CURRENT_DATE, "sysdate");            
            limitAndOffsetSymbols(false);
        }};
    }
    
    // TODO : test
    public static SqlOps forPostgreSQL(){
        return new SqlOps();
    }

    // TODO : test
    public static SqlOps forSQLServer(){
        return new SqlOps();
    }
    
           
}
