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
            add(Ops.OpMath.CEIL, "ceiling(%s)");
            add(Ops.OpMath.POWER, "power(%s,%s)");
            add(Ops.OpMath.RANDOM, "rand()");
            add(Ops.OpMath.ROUND, "round(%s,0)");
        }};        
    }
    
    public static SqlOps forMySQL(){
        return new SqlOps(){{
            add(Ops.OpMath.RANDOM, "rand()");
        }};
    }
    
    // TODO : PostgreSQL, Microsoft SQL Server, Oracle 9-11
        
}
