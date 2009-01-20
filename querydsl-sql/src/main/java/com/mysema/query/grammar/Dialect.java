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
    
    public static SqlOps forHqlsdb(){
        return new SqlOps(){{                                  
            add(Ops.OpMath.ROUND, "round(%s,0)");
            add(Ops.TRIM, "trim(both from %s)");            
        }};        
    }
    
    public static SqlOps forMySQL(){
        return new SqlOps(){{
            
        }};
    }
    
    // TODO : PostgreSQL, Microsoft SQL Server, Oracle 9-11
        
}
