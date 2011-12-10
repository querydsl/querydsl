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
package com.mysema.util;

import static org.junit.Assert.*;

import static java.util.Arrays.*;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;


public class MultiIteratorTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        List<Integer> list1 = asList(1, 2, 3, 4);
        List<Integer> list2 = asList(10, 20, 30);
        MultiIterator<Integer> iterator = new MultiIterator<Integer>(asList(list1, list2));
        List<Object[]> list = IteratorAdapter.asList(iterator);
        
        assertEquals(asList(1, 10), asList(list.get(0)));
        assertEquals(asList(1, 20), asList(list.get(1)));
        assertEquals(asList(1, 30), asList(list.get(2)));
        assertEquals(asList(2, 10), asList(list.get(3)));
        assertEquals(asList(2, 20), asList(list.get(4)));
        assertEquals(asList(2, 30), asList(list.get(5)));
        assertEquals(asList(3, 10), asList(list.get(6)));
        assertEquals(asList(3, 20), asList(list.get(7)));
        assertEquals(asList(3, 30), asList(list.get(8)));
        assertEquals(asList(4, 10), asList(list.get(9)));
        assertEquals(asList(4, 20), asList(list.get(10)));
        assertEquals(asList(4, 30), asList(list.get(11)));
    }

}
