package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.sql.CUBRIDTemplates;

public class CUBRIDSuiteTest extends AbstractSuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initCubrid();
        Connections.setTemplates(new CUBRIDTemplates(){{
            newLineToSingleSpace();
        }});
    }
    
}
