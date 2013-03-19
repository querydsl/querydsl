package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.H2Templates;

public class H2SuiteTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initH2();
        Connections.setTemplates(new H2Templates() {{
            newLineToSingleSpace();            
        }});
    }
        
}
