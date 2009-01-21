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
    
    // TODO : test
    public static SqlOps forPostgreSQL(){
        return new SqlOps();
    }

    // TODO : test
    public static SqlOps forSQLServer(){
        return new SqlOps();
    }
        
    // TODO : test
    public static SqlOps forOracle(){
        return new SqlOps();
    }
           
}
