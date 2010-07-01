package com.mysema.query._h2;

import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.ExportBaseTest;

public class ExportH2Test extends ExportBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }

}
