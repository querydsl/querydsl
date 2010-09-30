package com.mysema.query;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import jdepend.framework.JDepend;

import org.junit.Test;

public class DependenciesTest {
    
    @Test
    public void test() throws IOException{
        JDepend jdepend = new JDepend();        
        jdepend.addDirectory("target/classes/com/mysema/query/alias");
        jdepend.addDirectory("target/classes/com/mysema/query/codegen");
        jdepend.addDirectory("target/classes/com/mysema/query/dml");
        jdepend.addDirectory("target/classes/com/mysema/query/support");
        jdepend.addDirectory("target/classes/com/mysema/query/types");
        jdepend.addDirectory("target/classes/com/mysema/query/types/expr");
        jdepend.addDirectory("target/classes/com/mysema/query/types/path");
        jdepend.addDirectory("target/classes/com/mysema/query/types/query");
        jdepend.addDirectory("target/classes/com/mysema/query/types/template");

        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
