package com.mysema.query._cubrid;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

@Ignore
public class TypesCUBRIDTest extends TypesBaseTest {
    
    // not supported
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initCubrid();
    }
    
}
