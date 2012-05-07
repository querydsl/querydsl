package com.mysema.query.jpa.sql;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.sql.H2Templates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@JPAConfig("h2")
public class H2JPASQLTest extends AbstractJPASQLTest {
    
    public H2JPASQLTest() {
        super(new H2Templates());
    }
    
    @Test
    public void Count_Via_Unique(){
        // 
    }    
    
    @Test
    public void List_With_Offset(){
        // 
    }
    
    @Test
    public void Wildcard(){
        // 
    }

}
