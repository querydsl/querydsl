package com.mysema.query.suites;

import org.junit.BeforeClass;
import org.junit.Ignore;

import com.mysema.query.Mode;
import com.mysema.query.Target;

@Ignore
public class MySQLEclipseLinkTest extends AbstractSuite {
    
    @BeforeClass
    public static void setUp() throws Exception {
        Mode.mode.set("mysql-eclipselink");
        Mode.target.set(Target.MYSQL);
    }

}
