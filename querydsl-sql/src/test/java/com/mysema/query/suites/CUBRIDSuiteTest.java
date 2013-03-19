package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.query.Connections;
import com.mysema.query.sql.CUBRIDTemplates;
import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class CUBRIDSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initCubrid();
        Connections.setTemplates(new CUBRIDTemplates() {{
            newLineToSingleSpace();
        }});
    }
    
}
