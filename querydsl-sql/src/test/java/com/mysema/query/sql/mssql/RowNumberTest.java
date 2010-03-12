package com.mysema.query.sql.mssql;

import org.junit.Test;

import static com.mysema.query.Constants.*;
import static org.junit.Assert.*;

import static com.mysema.query.sql.mssql.SQLServerGrammar.*;

public class RowNumberTest {
    
//    ROW_NUMBER() OVER (ORDER BY OrderDate) AS 'RowNumber'
    
//    ROW_NUMBER() OVER( PARTITION BY PostalCode ORDER BY SalesYTD DESC)
    
    @Test
    public void test(){
        assertEquals(
            "row_number() over (order by e.firstname)", 
            rowNumber().orderBy(employee.firstname.asc()).toString());
        
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
