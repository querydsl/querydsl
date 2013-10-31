package com.mysema.query.suites;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.testutil.CustomSuite;

@RunWith(CustomSuite.class)
public abstract class AbstractSuite {

    @AfterClass
    public static void tearDown() throws SQLException {
        Connections.close();
    }

}