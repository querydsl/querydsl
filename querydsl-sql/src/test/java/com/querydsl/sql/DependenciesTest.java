/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

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
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/ddl");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/dml");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/mssql");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/mysql");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/oracle");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/support");
        jdepend.addDirectory("target/classes/com/mysema/querydsl/sql/types");

        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
