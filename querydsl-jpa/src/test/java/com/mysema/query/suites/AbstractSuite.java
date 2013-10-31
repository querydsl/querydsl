package com.mysema.query.suites;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.mysema.query.Mode;
import com.mysema.testutil.CustomSuite;

@RunWith(CustomSuite.class)
public abstract class AbstractSuite {

    @AfterClass
    public static void tearDownClass() throws Exception {
        Mode.mode.remove();
        Mode.target.remove();
    }

}
