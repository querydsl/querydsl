package com.mysema.query._oracle;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

public class TypesOracleTest extends TypesBaseTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initOracle();
    }
    
}
