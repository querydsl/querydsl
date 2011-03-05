package com.mysema.query.jdo;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import jdepend.framework.JDepend;

import org.junit.Ignore;
import org.junit.Test;

public class DependenciesTest {
    
    @Test
    @Ignore
    public void test() throws IOException{
        JDepend jdepend = new JDepend();        
        jdepend.addDirectory("target/classes/com/mysema/query/jdo");
        jdepend.addDirectory("target/classes/com/mysema/query/jdo/dml");
        jdepend.addDirectory("target/classes/com/mysema/query/jdo/sql");
        
        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
