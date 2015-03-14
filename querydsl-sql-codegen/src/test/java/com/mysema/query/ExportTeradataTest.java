package com.mysema.query;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.mysema.testutil.ExternalDB;

@Category(ExternalDB.class)
public class ExportTeradataTest extends ExportBaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initTeradata();
    }

}
