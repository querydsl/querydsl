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
package com.querydsl.core;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import jdepend.framework.JDepend;

public class DependenciesTest {
    
    @Test
    @Ignore
    public void test() throws IOException{
        // FIXME
        JDepend jdepend = new JDepend();        
        jdepend.addDirectory("target/classes/com/querydsl/alias");
        jdepend.addDirectory("target/classes/com/querydsl/codegen");
        jdepend.addDirectory("target/classes/com/querydsl/dml");
        jdepend.addDirectory("target/classes/com/querydsl/support");
        jdepend.addDirectory("target/classes/com/querydsl/types");
        jdepend.addDirectory("target/classes/com/querydsl/types/dsl");
        jdepend.addDirectory("target/classes/com/querydsl/types/path");
        jdepend.addDirectory("target/classes/com/querydsl/types/querydsl");
        jdepend.addDirectory("target/classes/com/querydsl/types/template");

        jdepend.analyze();    
        assertFalse(jdepend.containsCycles());

    }

}
