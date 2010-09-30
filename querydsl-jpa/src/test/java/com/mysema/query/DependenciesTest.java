package com.mysema.query;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import jdepend.framework.JDepend;

import org.junit.Test;

public class DependenciesTest {
    
    @Test
    public void test() throws IOException{
        JDepend jdepend = new JDepend();        
        jdepend.addDirectory("target/classes/com/mysema/query/jpa");
        jdepend.addDirectory("target/classes/com/mysema/query/jpa/hibernate");
        jdepend.addDirectory("target/classes/com/mysema/query/jpa/hibernate/sql");
        jdepend.addDirectory("target/classes/com/mysema/query/jpa/impl");
        jdepend.addDirectory("target/classes/com/mysema/query/jpa/sql");

        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
