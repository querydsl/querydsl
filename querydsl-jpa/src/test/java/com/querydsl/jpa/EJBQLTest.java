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
package com.querydsl.jpa;

import org.junit.Test;

public class EJBQLTest extends AbstractQueryTest{

    // Any function or operator defined by EJB-QL 3.0: substring(), trim(),
    // lower(), upper(), length(), locate(), abs(), sqrt(), bit_length(),
    // mod()
    // substring(),
    // trim(),
    // lower(),
    // upper(),
    // length(),
    // locate(),
    // abs(),
    // sqrt(),
    // bit_length(),
    // mod()
    
    @Test
    public void Trim() {
        assertToString("trim(cat.name)", cat.name.trim());   
    }
    
    @Test
    public void Lower() {
        assertToString("lower(cat.name)", cat.name.lower());    
    }
    
    @Test
    public void Upper() {
        assertToString("upper(cat.name)", cat.name.upper());   
    }

}
