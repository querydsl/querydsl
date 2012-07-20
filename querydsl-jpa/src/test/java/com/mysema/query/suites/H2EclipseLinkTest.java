package com.mysema.query.suites;

import org.junit.BeforeClass;

import com.mysema.query.Mode;
import com.mysema.query.Target;

public class H2EclipseLinkTest extends AbstractJPASuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("h2-eclipselink");
        Mode.target.set(Target.H2);
    }

}
