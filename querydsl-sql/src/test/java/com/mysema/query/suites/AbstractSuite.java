package com.mysema.query.suites;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;

@RunWith(CustomSuite.class)
public abstract class AbstractSuite {

    @AfterClass
    public static void tearDown() throws SQLException {
        Connections.close();
    }

}