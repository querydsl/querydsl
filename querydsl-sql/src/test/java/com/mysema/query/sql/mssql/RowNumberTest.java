/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.mssql;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rn;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rowNumber;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RowNumberTest {
    
//    ROW_NUMBER() OVER (ORDER BY OrderDate) AS 'RowNumber'
    
//    ROW_NUMBER() OVER( PARTITION BY PostalCode ORDER BY SalesYTD DESC)
    
    @Test
    public void test(){
        assertEquals(
            "row_number() over (order by e.firstname)", 
            rowNumber().orderBy(employee.firstname.asc()).toString());
        
        assertEquals(
            "row_number() over (order by e.firstname)", 
            rowNumber().orderBy(employee.firstname).toString());
        
        assertEquals(
            "row_number() over (order by e.firstname) as rn", 
            rowNumber().orderBy(employee.firstname.asc()).as(rn).toString());
        
        assertEquals(
            "row_number() over (order by e.firstname desc)", 
            rowNumber().orderBy(employee.firstname.desc()).toString());
        
        assertEquals(
            "row_number() over (partition by e.lastname order by e.firstname)", 
            rowNumber().partitionBy(employee.lastname).orderBy(employee.firstname.asc()).toString());
        
        assertEquals(
            "row_number() over (partition by e.lastname, e.firstname order by e.firstname)", 
            rowNumber().partitionBy(employee.lastname, employee.firstname).orderBy(employee.firstname.asc()).toString());
    }
    

}
