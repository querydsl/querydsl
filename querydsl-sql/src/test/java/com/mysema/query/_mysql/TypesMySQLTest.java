package com.mysema.query._mysql;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.TypesBaseTest;

public class TypesMySQLTest extends TypesBaseTest {
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initMySQL();
    }
    
}
