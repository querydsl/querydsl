package com.mysema.query.sql.mssql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.mssql.DatePart;
import com.mysema.query.sql.mssql.SQLServerGrammar;

public class SQLServerGrammarTest {
    
    @Test
    public void Datediff() {
        assertEquals("datediff(day,start,end)", 
                SQLServerGrammar.datediff(DatePart.day, "start", "end").toString());
    }
    
    @Test
    public void Dateadd() {
        assertEquals("dateadd(hour,3,start)", 
                SQLServerGrammar.dateadd(DatePart.hour, 3, "start").toString());
    }
    
}
