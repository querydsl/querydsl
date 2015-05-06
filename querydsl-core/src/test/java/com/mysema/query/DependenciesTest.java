/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
        // FIXME
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
