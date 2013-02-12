package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class H2BatooTest extends AbstractJPASuite {

    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("h2-batoo");
        Mode.target.set(Target.H2);
    }

}
