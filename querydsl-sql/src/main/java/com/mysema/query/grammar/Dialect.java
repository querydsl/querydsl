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
        return new SqlOps(){
//            add(Ops.OpMath.CEIL, "ceiling(%s)");
            
        };
    }
    
    // TODO : MySQL, PostgreSQL, Microsoft SQL Server, Oracle 9-11
        
}
