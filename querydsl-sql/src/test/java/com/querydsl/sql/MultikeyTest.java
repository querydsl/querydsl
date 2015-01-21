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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MultikeyTest {

    Multikey multiKey1 = new Multikey();
    Multikey multiKey2 = new Multikey();
    
    @Test
    public void HashCode() {
        int hashCode = multiKey1.hashCode();
        multiKey1.setId(1);
        assertEquals(hashCode, multiKey1.hashCode());
        
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        
        multiKey2.setId(1);
        multiKey2.setId2("2");
        multiKey2.setId3(3);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());                
    }
    
    @Test
    public void Equals() {
        multiKey1.setId(1);
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        
        assertFalse(multiKey1.equals(multiKey2));
        multiKey2.setId(1);        
        assertFalse(multiKey1.equals(multiKey2));
        
        multiKey2.setId2("2");
        multiKey2.setId3(3);
        
        assertEquals(multiKey1, multiKey2);
    }
    
    @Test
    public void ToString() {
        assertEquals("Multikey#null;null;null", multiKey1.toString());
        
        multiKey1.setId(1);
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        assertEquals("Multikey#1;2;3", multiKey1.toString());
    }
    
}
