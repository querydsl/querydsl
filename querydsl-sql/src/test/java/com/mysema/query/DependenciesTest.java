package com.mysema.query;

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
        jdepend.addDirectory("target/classes/com/mysema/query/sql");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/ddl");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/dml");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/mssql");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/mysql");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/oracle");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/support");
        jdepend.addDirectory("target/classes/com/mysema/query/sql/types");

        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
