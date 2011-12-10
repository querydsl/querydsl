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
package com.mysema.query.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.QueryModifiers;


public class LimitingIteratorTest {

    @Test
    public void Limit(){
        LimitingIterator<String> it = new LimitingIterator<String>(Arrays.asList("1","2","3").iterator(), 2);
        assertEquals(Arrays.asList("1","2"), IteratorAdapter.asList(it));
    }

    @Test
    public void Offset(){
        Iterator<String> it = LimitingIterator.create(Arrays.asList("1","2","3").iterator(), QueryModifiers.offset(1));
        assertEquals(Arrays.asList("2","3"), IteratorAdapter.asList(it));
    }

}
