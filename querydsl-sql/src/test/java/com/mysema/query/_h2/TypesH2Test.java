package com.mysema.query._h2;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

public class TypesH2Test extends TypesBaseTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }
    
}
